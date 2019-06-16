package com.mk.orderbooks.controller.validation;

import com.mk.orderbooks.controller.request.NewOrderRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.math.BigDecimal.ZERO;

public class MarketOrderValidator implements ConstraintValidator<MarketOrderConstraint, NewOrderRequest> {

    @Override
    public boolean isValid(NewOrderRequest newOrderRequest, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValidMarketOrder = newOrderRequest.isMarketOrder() && (newOrderRequest.getPrice() == null || newOrderRequest.getPrice().equals(ZERO));
        boolean isValidLimitOrder = !newOrderRequest.isMarketOrder() && (newOrderRequest.getPrice() != null && newOrderRequest.getPrice().compareTo(ZERO) == 1);
        return isValidMarketOrder || isValidLimitOrder;
    }
}
