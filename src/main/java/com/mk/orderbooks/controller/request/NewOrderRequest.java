package com.mk.orderbooks.controller.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewOrderRequest {

    private final int quantity;
    private final BigDecimal price;

}
