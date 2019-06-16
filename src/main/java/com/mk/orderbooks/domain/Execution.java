package com.mk.orderbooks.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Execution {

    private final String id;
    private int quantity;
    private final BigDecimal price = BigDecimal.valueOf(50L);
}
