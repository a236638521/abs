package com.m7.abs.common.annotation;

import java.lang.annotation.*;

/**
 * 工具箱
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ToolBoxType {
    String name();
}
