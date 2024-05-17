package com.m7.abs.common.constant.nsq;

import com.m7.abs.common.annotation.NsqListener;
import com.sproutsocial.nsq.Message;
import com.sproutsocial.nsq.MessageDataHandler;
import com.sproutsocial.nsq.MessageHandler;
import com.sproutsocial.nsq.Subscriber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.lang.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author zhuhf
 */
public class NsqListenerBeanPostProcessor
        implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<ApplicationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(NsqListenerBeanPostProcessor.class);

    private BeanExpressionResolver resolver = new StandardBeanExpressionResolver();

    private BeanExpressionContext expressionContext;

    private BeanFactory beanFactory;

    private final List<NsqListenerEndpoint> nsqListenerEndpointList = new CopyOnWriteArrayList<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, @NonNull String beanName) throws BeansException {
        List<NsqListenerEndpoint> endpointList = Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(NsqListener.class))
                .map(method -> new NsqListenerEndpoint(method, bean))
                .collect(Collectors.toList());
        nsqListenerEndpointList.addAll(endpointList);

        return bean;
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.resolver = ((ConfigurableListableBeanFactory) beanFactory).getBeanExpressionResolver();
            this.expressionContext = new BeanExpressionContext((ConfigurableListableBeanFactory) beanFactory, null);
        }
    }

    private String resolve(String value) {
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value);
        }
        return value;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {

        if (event instanceof ApplicationReadyEvent) {
            logger.info("收到 ApplicationReadyEvent，启动订阅");
            refreshSubscribe();
            return;
        }
        if (event instanceof RefreshScopeRefreshedEvent) {
            logger.info("收到 RefreshScopeRefreshedEvent，更新订阅");
            refreshSubscribe();
        }
    }

    private void refreshSubscribe() {
        nsqListenerEndpointList.forEach(nsqListenerEndpoint -> {
            Method method = nsqListenerEndpoint.getMethod();
            method.setAccessible(true);
            Object bean = nsqListenerEndpoint.getBean();
            NsqListener nsqListener = method.getAnnotation(NsqListener.class);
            List<Class<?>> paramClassList = Arrays.asList(method.getParameterTypes());
            boolean matchMessage = paramClassList.contains(Message.class);

            String topic = (String) this.resolver.evaluate(resolve(nsqListener.topic()), this.expressionContext);
            String channel = (String) this.resolver.evaluate(resolve(nsqListener.channel()), this.expressionContext);
            if (matchMessage) {
                if (paramClassList.size() != 1) {
                    logger.error("[{}]标记的方法[{}]除了[{}]类型参数外还存在其他参数", NsqListener.class, method, Message.class);
                }
                beanFactory.getBean(Subscriber.class).subscribe(topic, channel, (MessageHandler) msg -> {
                    try {
                        method.invoke(bean, msg);
                    } catch (Exception e) {
                        logger.error("消息处理方法执行异常", e);
                    }
                });
                logger.info(
                        "订阅 topic[{}]-channel[{}], handler bean-method: [{}-{}]",
                        topic,
                        channel,
                        bean.getClass(),
                        method);
            }
            boolean matchMessageData = paramClassList.contains(String.class);
            if (matchMessageData) {
                if (paramClassList.size() != 1) {
                    logger.error("[{}]标记的方法[{}]除了[{}]类型参数外还存在其他参数", NsqListener.class, method, String.class);
                }
                beanFactory.getBean(Subscriber.class).subscribe(topic, channel, (MessageDataHandler) data -> {
                    try {
                        method.invoke(bean, new String(data, StandardCharsets.UTF_8));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("", e);
                    }
                });
                logger.info(
                        "订阅 topic[{}]-channel[{}], handler bean-method: [{}-{}]",
                        topic,
                        channel,
                        bean.getClass(),
                        method);
            }
        });
    }

    @AllArgsConstructor
    @Getter
    private static class NsqListenerEndpoint {
        private Method method;
        private Object bean;
    }
}
