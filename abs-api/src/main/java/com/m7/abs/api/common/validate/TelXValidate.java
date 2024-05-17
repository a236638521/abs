package com.m7.abs.api.common.validate;

import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TelXValidate {
    String message() default "没有这个类型，请重新选择";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
