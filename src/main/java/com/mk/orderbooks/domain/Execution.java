package com.mk.orderbooks.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Execution {

    private int quantity;
    private BigDecimal price;
}
