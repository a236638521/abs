package com.m7.abs.api.projectHandler.flashSm;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface FlashSmType {
    String channel();
}
