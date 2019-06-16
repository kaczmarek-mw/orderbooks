package com.mk.orderbooks.service;

import com.mk.orderbooks.domain.Execution;
import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.Order;
import com.mk.orderbooks.domain.OrderBook;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface OrderBookService {
    List<OrderBook> getOrderBooks();

    OrderBook getOrderBook(String id);

    OrderBook closeOrderBook(String id);

    OrderBook openOrderBook(FinancialInstrument financialInstrument);

    Order addOrder(String orderBookId, int quantity, boolean isMarketOrder, BigDecimal price);

    Collection<Order> getOrders(String orderBookId);

    Order getOrder(String orderBookId, String orderId);

    Execution addExecution(String orderBookId, int quantity);

    Collection<Execution> getExecutions(String orderBookId);

    Execution getExecution(String orderBookId, String executionId);

}
