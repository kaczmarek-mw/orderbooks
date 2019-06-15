package com.mk.orderbooks.service;

import com.mk.orderbooks.domain.OrderBook;

import java.util.List;

public interface MarketService {
    List<OrderBook> getOrderBooks();

    OrderBook getOrderBookById(String id);

    boolean closeOrderBook(String id);
}
