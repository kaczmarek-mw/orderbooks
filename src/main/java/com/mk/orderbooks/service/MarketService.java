package com.mk.orderbooks.service;

import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.OrderBook;

import java.util.List;

public interface MarketService {
    List<OrderBook> getOrderBooks();

    OrderBook getOrderBookById(String id);

    OrderBook closeOrderBook(String id);

    OrderBook openOrderBook(FinancialInstrument financialInstrument);
}
