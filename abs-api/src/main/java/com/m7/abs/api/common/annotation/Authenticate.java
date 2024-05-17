package com.m7.abs.api.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Authenticate {
    /**
     * 是否需要校验访问权限 默认不校验
     *
     * @return
     */
    boolean permission() default false;

    /**
     * 是否校验IP地址  默认不校验
     *
     * @return
     */
    boolean checkIp() default false;
}
