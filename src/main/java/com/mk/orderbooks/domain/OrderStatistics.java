package com.mk.orderbooks.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderStatistics {

    private boolean isValid;
    private int initialQuantity;
    private int executedQuantity;
    private BigDecimal price;
    private String executionPrice;

}
