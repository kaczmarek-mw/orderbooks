package com.mk.orderbooks.domain.mutable;

import com.mk.orderbooks.domain.Execution;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MutableExecution {

    public MutableExecution(Execution execution) {
        this.id = execution.getId();
        this.quantity = execution.getQuantity();
        this.price = execution.getPrice();
    }

    private String id;
    private int quantity;
    private BigDecimal price;

    public void subtractQuantity(int quantity) {
        int possibleQuant = Math.min(this.quantity, quantity);
        this.quantity -= possibleQuant;
    }
}
