package com.mk.orderbooks.domain.processor;

import com.mk.orderbooks.domain.mutable.MutableExecution;
import com.mk.orderbooks.domain.mutable.MutableOrder;
import com.mk.orderbooks.domain.mutable.MutableOrderBook;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.floor;
import static java.util.stream.Collectors.toList;

@Service
public class OrderBookProcessor {

    public boolean runExecutions(MutableOrderBook orderBook) {
        if (orderBook == null) {
            throw new IllegalStateException("Order book is null");
        }
        if (orderBook.getExecutions().isEmpty()) {
            throw new IllegalArgumentException("There are no executions in the order book");
        }
        if (orderBook.getOrders().isEmpty()) {
            throw new IllegalArgumentException("There are no orders in the order book");
        }

        getOpenExecutions(orderBook.getExecutions()).forEach(execution -> distributeExecution(getOpenOrders(orderBook.getOrders()), execution));
        boolean isExecuted = getOpenExecutions(orderBook.getExecutions()).isEmpty();
        orderBook.setExecuted(isExecuted);
        return isExecuted;
    }

    void distributeExecution(Collection<MutableOrder> orders, MutableExecution execution) {
        while (!getValidOrders(orders, execution.getPrice()).isEmpty() && execution.getQuantity() > 0) {
            Collection<MutableOrder> remainingValidOrders = getValidOrders(orders, execution.getPrice());
            final int evenDistribution = getEvenDistribution(execution.getQuantity(), remainingValidOrders.size());
            remainingValidOrders.forEach(order -> {
                int minimalDistribution = Math.max(evenDistribution, 1);
                executeOrder(order, execution, minimalDistribution);
            });
        }
    }

    void executeOrder(MutableOrder order, MutableExecution execution, int purchase) {
        if(purchase > execution.getQuantity()) {
            throw new IllegalStateException("Unable to execute this purchase");
        }
        execution.substractQuantity(purchase - order.substractQuantity(purchase));
    }

    List<MutableExecution> getOpenExecutions(Collection<MutableExecution> executions) {
        return executions.stream().filter(execution -> execution.getQuantity() > 0).collect(toList());
    }

    List<MutableOrder> getOpenOrders(Collection<MutableOrder> orders) {
        return orders.stream().filter(order -> order.getQuantity() > 0).collect(toList());
    }

    List<MutableOrder> getValidOrders(Collection<MutableOrder> orders, BigDecimal executionPrice) {
        return getOpenOrders(orders).stream().filter(order -> order.isMarketOrder() || order.getPrice().compareTo(executionPrice) < 0).collect(toList());
    }

    int getEvenDistribution(int executionQuantity, int ordersCount) {
        return (int) floor(executionQuantity / ordersCount);
    }

}
