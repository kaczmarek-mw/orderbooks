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
        OrderBookStatistics statistics = OrderBookStatistics.builder().build();

        statistics.setOrderCount(orderBook.getOrders().size());
        statistics.setValidOrderCount(orderBookProcessor.getValidOrders(orderBook).size());
        statistics.setInvalidOrderCount(orderBook.getOrders().stream().filter(order -> !order.isValid()).count());
        statistics.setOpenOrderCount(orderBookProcessor.getOpenOrders(orderBook.getOrders()).size());
        statistics.setClosedOrderCount(orderBook.getOrders().stream().filter(order -> order.getQuantity() == 0).count());
        statistics.setOpenExecutionsCount(orderBookProcessor.getOpenExecutions(orderBook.getExecutions()).size());
        statistics.setClosedExecutionsCount(orderBook.getExecutions().stream().filter(execution -> execution.getQuantity() == 0).count());
        statistics.setDemand(orderBook.getOrders().stream().mapToInt(MutableOrder::getQuantity).sum());
        statistics.setInvalidDemand(orderBook.getOrders().stream().filter(order -> !order.isValid()).mapToInt(MutableOrder::getQuantity).sum());
        statistics.setValidDemand(orderBook.getOrders().stream().filter(MutableOrder::isValid).mapToInt(MutableOrder::getQuantity).sum());
        statistics.setBiggestOrder(orderBook.getOrders().stream().min((order1, order2) -> order2.getQuantity() - order1.getQuantity()).get().getQuantity());
        statistics.setSmallestOrder(orderBook.getOrders().stream().max((order1, order2) -> order2.getQuantity() - order1.getQuantity()).get().getQuantity());
        statistics.setEarliestOrderEntry(orderBook.getOrders().stream().max((order1, order2) -> order2.getEntryTime().compareTo(order1.getEntryTime())).get().getEntryTime());
        statistics.setLastOrderEntry(orderBook.getOrders().stream().min((order1, order2) -> order2.getEntryTime().compareTo(order1.getEntryTime())).get().getEntryTime());
        statistics.setExecutionQuantity(orderBook.getExecutions().stream().mapToInt(MutableExecution::getQuantity).sum());
        statistics.setExecutionPrices(orderBook.getExecutions().stream().map(MutableExecution::getPrice).collect(toSet()));
        statistics.setLimitBreakdown(orderBook.getOrders().stream()
                .filter(order -> !order.isMarketOrder())
                .collect(groupingBy(MutableOrder::getPrice, summarizingInt(MutableOrder::getQuantity))));

        return statistics;
    }


}
