package com.mk.orderbooks.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class OrderBook {

    private final String id;
    private boolean isOpen;
    private final FinancialInstrument financialInstrument;
    private final Collection<Order> orders;
    private final Collection<Execution> executions;


}
