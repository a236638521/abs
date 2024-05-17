package com.m7.abs.common.constant.nsq;

import com.m7.abs.common.properties.nsq.NsqProperties;
import com.m7.abs.common.properties.nsq.PublisherProperties;
import com.m7.abs.common.properties.nsq.SubscriberProperties;
import com.sproutsocial.nsq.Client;
import com.sproutsocial.nsq.Publisher;
import com.sproutsocial.nsq.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 使用方式：在方法上加 {@link com.m7.abs.common.annotation.NsqListener} 注解，方法的参数有且只能有一个，
 * 且类型必须为 {@link String} 或者 {@link com.sproutsocial.nsq.Message}
 *
 * @author zhuhf
 */
@Configuration
@RefreshScope
@ConditionalOnClass(name = "com.sproutsocial.nsq.Client")
public class NsqConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(NsqConfiguration.class);
    private static Client client = null;

    @Bean
    public NsqProperties nsqProperties() {
        return new NsqProperties();
    }

    @Configuration
    @ConditionalOnExpression(value = "#{${v7.nsq.publisher.enable:true}}")
    public static class PublisherAutoConfiguration {
        public static final String PUBLISHER_BEAN_NAME = "publisher";

        @Bean(name = PUBLISHER_BEAN_NAME)
        @RefreshScope
        @ConditionalOnMissingBean(name = PUBLISHER_BEAN_NAME)
        public FactoryBean<Publisher> publisher(NsqProperties nsqProperties) {
            PublisherProperties publisherProperties = nsqProperties.getPublisher();
            if (publisherProperties.getNsqdHost().isEmpty()) {
                throw new RuntimeException("v7.nsq.publisher.nsqdHost 配置为空");
            }

            return new PublisherFactoryBean(nsqProperties);
        }
    }

    @Configuration
    @ConditionalOnExpression(value = "#{${v7.nsq.subscriber.enable:true}}")
    public static class SubscriberAutoConfiguration {

        @Bean(destroyMethod = "stop")
        @RefreshScope
        @ConditionalOnMissingBean
        public Subscriber subscriber(NsqProperties nsqProperties) {
            SubscriberProperties subscriberProperties = nsqProperties.getSubscriber();
            Subscriber subscriber =
                    new Subscriber(subscriberProperties.getLookupdHosts().toArray(new String[0]));
            subscriber.setConfig(nsqProperties.getConfig());
            return subscriber;
        }

        @Bean
        @ConditionalOnMissingBean
        public NsqListenerBeanPostProcessor nsqListenerBeanPostProcessor() {
            return new NsqListenerBeanPostProcessor();
        }
    }

    public static class PublisherFactoryBean implements FactoryBean<Publisher>, DisposableBean {
        private static final Logger logger = LoggerFactory.getLogger(PublisherFactoryBean.class);
        private final List<Publisher> publisherList;

        public PublisherFactoryBean(NsqProperties nsqProperties) {
            if (client == null) {
                client = new Client();
            }

            List<String> publisherList = nsqProperties.getPublisher().getNsqdHost();
            int size = publisherList.size();
            ArrayList<Publisher> publishers = new ArrayList<>(size % 2 + 1);
            for (int i = 0; i < size; ) {
                Publisher publisher;
                if (i < size - 2) {
                    String addressPre = publisherList.get(i);
                    String addressSuf = publisherList.get(i + 1);
                    publisher = new Publisher(client, addressPre, addressSuf);
                    i += 2;
                } else {
                    String address = publisherList.get(i);
                    publisher = new Publisher(client, address, null);
                    i++;
                }
                publisher.setConfig(nsqProperties.getConfig());
                publishers.add(publisher);
            }
            this.publisherList = publishers;
        }

        @Override
        public Publisher getObject() {
            return publisherList.get(ThreadLocalRandom.current().nextInt(0, publisherList.size()));
        }

        @Override
        public Class<?> getObjectType() {
            return Publisher.class;
        }

        @Override
        public void destroy() {
            logger.info("publishers stopping");
            publisherList.forEach(Publisher::stop);
            if (client != null) {
                client.stop();
                client = null;
            }
            logger.info("publishers stopped");
        }
    }
}
