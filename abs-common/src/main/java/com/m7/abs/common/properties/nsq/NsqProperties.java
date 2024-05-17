package com.m7.abs.common.properties.nsq;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author zhuhf
 */
@ConditionalOnClass(name = "com.sproutsocial.nsq.Client")
@RefreshScope
@ConfigurationProperties(prefix = "v7.nsq")
@Data
@NoArgsConstructor
public class NsqProperties {
    /**
     * 订阅配置
     */
    @NestedConfigurationProperty
    private SubscriberProperties subscriber = new SubscriberProperties();

    /**
     * 发布配置
     */
    @NestedConfigurationProperty
    private PublisherProperties publisher = new PublisherProperties();

    /**
     * nsq 配置
     */
    @NestedConfigurationProperty
    private ServerConfigProperties config;
}
