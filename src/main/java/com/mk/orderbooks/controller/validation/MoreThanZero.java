package com.mk.orderbooks.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MoreThanZeroValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MoreThanZero {

    String message() default "Value must be more than zero";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}