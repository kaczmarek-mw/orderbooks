package com.mk.orderbooks.domain;

import com.mk.orderbooks.domain.mutable.MutableExecution;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Execution {

    private final String id;
    private final int quantity;
    // Requirement said that all executions have the same price.
    private final BigDecimal price = BigDecimal.valueOf(20L);

    public MutableExecution toMutable() {
        return new MutableExecution(this);
    }
}
