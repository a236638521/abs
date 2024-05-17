package com.m7.abs.api.common.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TelXValidator implements ConstraintValidator<TelXValidate,String> {
    @Override
    public void initialize(TelXValidate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
