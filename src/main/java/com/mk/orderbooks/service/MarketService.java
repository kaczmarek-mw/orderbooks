package com.mk.orderbooks.service;

import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.Order;
import com.mk.orderbooks.domain.OrderBook;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface MarketService {
    List<OrderBook> getOrderBooks();

    OrderBook getOrderBookById(String id);

    OrderBook closeOrderBook(String id);

    OrderBook openOrderBook(FinancialInstrument financialInstrument);

    Order addOrder(String orderBookId, int quantity, BigDecimal price);

    Collection<Order> getOrdersByOrderBookId(String orderBookId);

    Order getOrderByOrderBookIdAndOrderId(String orderBookId, String orderId);
}
