package com.m7.abs.common.annotation;


import java.lang.annotation.*;

/**
 *
 * @author KejiePeng
 */
@Retention(RetentionPolicy.RUNTIME)//注解不仅被保存到class文件中，jvm加载class文件之后，仍存在
@Target(ElementType.METHOD) //注解添加的位置
@Documented
public @interface WebAspect {
    /**
     * 日志描述
     * @return
     */
    String logDesc() default "";

    /**
     * 自动注入requestId
     * @return
     */
    boolean injectReqId() default false;

    /**
     * 是否打印日志详情
     * @return
     */
    boolean logDetail() default true;
}
