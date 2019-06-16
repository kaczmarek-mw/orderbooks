package com.mk.orderbooks.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class NewOrderRequest {

    @NotNull(message = "quantity must be given!")
    private Integer quantity;
    private boolean isMarketOrder;
    private BigDecimal price;

}
