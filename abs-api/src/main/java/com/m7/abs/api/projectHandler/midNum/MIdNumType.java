package com.m7.abs.api.projectHandler.midNum;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface MIdNumType {
    String channel();
}
