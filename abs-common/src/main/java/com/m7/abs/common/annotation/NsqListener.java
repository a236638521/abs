package com.m7.abs.common.annotation;

import java.lang.annotation.*;

/**
 * @author zhuhf
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NsqListener {
    /**
     * 支持 spel 表达式，如 ${v7.nsq.subscriber.sms-receipt.topic}
     */
    String topic();

    /**
     * 支持 spel 表达式，如 ${v7.nsq.subscriber.sms-receipt.channel}
     */
    String channel();
}
