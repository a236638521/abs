package com.m7.abs.common.annotation;

import com.m7.abs.common.constant.common.DisScheduleUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisSchedule {

    /**
     * 定时调度任务的名称(默认是方法名)
     */
    String name() default "";

    /**
     * 任务的间隔时间,单位/秒
     */
    int duration();

    /**
     * duration的时间单位(默认：分钟)
     */
    DisScheduleUnit unit() default DisScheduleUnit.MINUTES;
}
