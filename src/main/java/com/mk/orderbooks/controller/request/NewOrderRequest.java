package com.mk.orderbooks.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class NewOrderRequest {

    @NotNull(message = "quantity must be given!")
    private final Integer quantity;
    private final boolean isMarketOrder;
    private final BigDecimal price;

}
