package com.mk.orderbooks.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MarketOrderValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MarketOrderConstraint {

    String message() default "Invalid combination of price and 'isMarketOrder' flag";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}