package com.mk.orderbooks.domain;

import com.mk.orderbooks.domain.mutable.MutableOrderBook;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class OrderBook {

    private final String id;
    private boolean isOpen;
    // The requirement said that financialInstrument belongs to Order, but it made more sense to me that it belongs here.
    private final FinancialInstrument financialInstrument;
    private final Collection<Order> orders;
    private final Collection<Execution> executions;


    public MutableOrderBook toMutable() {
        return new MutableOrderBook(this);
    }
}
