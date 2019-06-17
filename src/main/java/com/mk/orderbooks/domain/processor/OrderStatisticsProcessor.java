package com.mk.orderbooks.domain.processor;


import com.mk.orderbooks.domain.Order;
import com.mk.orderbooks.domain.OrderStatistics;
import com.mk.orderbooks.domain.mutable.MutableOrder;
import com.mk.orderbooks.domain.mutable.MutableOrderBook;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderStatisticsProcessor {

    private final OrderBookProcessor orderBookProcessor;

    public OrderStatisticsProcessor(OrderBookProcessor orderBookProcessor) {
        this.orderBookProcessor = orderBookProcessor;
    }

    public OrderStatistics collectStatistics(MutableOrderBook orderBook, Order order) {

        Optional<MutableOrder> maybeOrder = orderBook.getOrders().stream().filter(mo -> mo.getId().equals(order.getId())).findFirst();
        if (!maybeOrder.isPresent()) {
            throw new NoSuchElementException();
        }
        MutableOrder mutableOrder = maybeOrder.get();

        OrderStatistics statistics = OrderStatistics.builder()
                .price(order.getPrice())
                .executionPrice("Not sure what execution price would be since there are many executions affecting single order. I would need to clarify this before implementation.")
                .initialQuantity(order.getQuantity())
                .executedQuantity(order.getQuantity() - mutableOrder.getQuantity())
                .isValid(mutableOrder.isValid())
                .build();
        return statistics;
    }


}
