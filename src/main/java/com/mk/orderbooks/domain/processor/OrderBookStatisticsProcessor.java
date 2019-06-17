package com.mk.orderbooks.domain.processor;


import com.mk.orderbooks.domain.OrderBookStatistics;
import com.mk.orderbooks.domain.mutable.MutableExecution;
import com.mk.orderbooks.domain.mutable.MutableOrder;
import com.mk.orderbooks.domain.mutable.MutableOrderBook;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.*;

@Service
public class OrderBookStatisticsProcessor {

    private final OrderBookProcessor orderBookProcessor;

    public OrderBookStatisticsProcessor(OrderBookProcessor orderBookProcessor) {
        this.orderBookProcessor = orderBookProcessor;
    }

    public OrderBookStatistics collectStatistics(MutableOrderBook orderBook) {
        return OrderBookStatistics.builder()
                .orderCount(orderBook.getOrders().size())
                .validOrderCount(orderBookProcessor.getValidOrders(orderBook).size())
                .invalidOrderCount(orderBook.getOrders().stream().filter(order -> !order.isValid()).count())
                .openOrderCount(orderBookProcessor.getOpenOrders(orderBook.getOrders()).size())
                .closedOrderCount(orderBook.getOrders().stream().filter(order -> order.getQuantity() == 0).count())
                .openExecutionsCount(orderBookProcessor.getOpenExecutions(orderBook.getExecutions()).size())
                .closedExecutionsCount(orderBook.getExecutions().stream().filter(execution -> execution.getQuantity() == 0).count())
                .demand(orderBook.getOrders().stream().mapToInt(MutableOrder::getQuantity).sum())
                .invalidDemand(orderBook.getOrders().stream().filter(order -> !order.isValid()).mapToInt(MutableOrder::getQuantity).sum())
                .validDemand(orderBook.getOrders().stream().filter(MutableOrder::isValid).mapToInt(MutableOrder::getQuantity).sum())
                .biggestOrder(orderBook.getOrders().stream().min((order1, order2) -> order2.getQuantity() - order1.getQuantity()).get().getQuantity())
                .smallestOrder(orderBook.getOrders().stream().max((order1, order2) -> order2.getQuantity() - order1.getQuantity()).get().getQuantity())
                .earliestOrderEntry(orderBook.getOrders().stream().max((order1, order2) -> order2.getEntryTime().compareTo(order1.getEntryTime())).get().getEntryTime())
                .lastOrderEntry(orderBook.getOrders().stream().min((order1, order2) -> order2.getEntryTime().compareTo(order1.getEntryTime())).get().getEntryTime())
                .executionQuantity(orderBook.getExecutions().stream().mapToInt(MutableExecution::getQuantity).sum())
                .executionPrices(orderBook.getExecutions().stream().map(MutableExecution::getPrice).collect(toSet()))
                .limitBreakdown(orderBook.getOrders().stream()
                        .filter(order -> !order.isMarketOrder())
                        .collect(groupingBy(MutableOrder::getPrice, summarizingInt(MutableOrder::getQuantity))))
                .build();
    }


}
