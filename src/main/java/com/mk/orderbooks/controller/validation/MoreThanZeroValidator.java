package com.mk.orderbooks.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MoreThanZeroValidator implements ConstraintValidator<MoreThanZero, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && value > 0;
    }
}
