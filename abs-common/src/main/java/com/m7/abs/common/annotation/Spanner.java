package com.m7.abs.common.annotation;

import java.lang.annotation.*;

/**
 * 扳手
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Spanner {
    String name();
}
