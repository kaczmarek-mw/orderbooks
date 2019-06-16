package com.mk.orderbooks.domain.mutable;

import com.mk.orderbooks.domain.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class MutableOrder {

    public MutableOrder(Order order) {
        this.id = order.getId();
        this.quantity = order.getQuantity();
        this.entryTime = order.getEntryTime();
        this.isMarketOrder = order.isMarketOrder();
        this.price = order.getPrice();
    }

    private String id;
    private int quantity;
    private Instant entryTime;
    private boolean isMarketOrder;
    private BigDecimal price;

    public int substractQuantity(int quantity) {
        int possibleQuant = Math.min(this.quantity, quantity);
        this.quantity -= possibleQuant;
        return quantity - possibleQuant;
    }
}
