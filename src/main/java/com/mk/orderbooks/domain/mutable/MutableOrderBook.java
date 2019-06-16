package com.mk.orderbooks.domain.mutable;

import com.mk.orderbooks.domain.Execution;
import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.Order;
import com.mk.orderbooks.domain.OrderBook;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MutableOrderBook {

    private String id;
    private boolean isOpen;
    private boolean isExecuted;
    private FinancialInstrument financialInstrument;
    private Collection<MutableOrder> orders;
    private Collection<MutableExecution> executions;

    public MutableOrderBook(OrderBook orderBook) {
        this.id = orderBook.getId();
        this.isOpen = orderBook.isOpen();
        this.financialInstrument = orderBook.getFinancialInstrument();
        this.orders = orderBook.getOrders().stream().map(Order::toMutable).collect(Collectors.toList());
        this.executions = orderBook.getExecutions().stream().map(Execution::toMutable).collect(Collectors.toList());
    }

}
