package com.mk.orderbooks.service;

import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.Market;
import com.mk.orderbooks.domain.Order;
import com.mk.orderbooks.domain.OrderBook;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.math.BigDecimal.TEN;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

@Service
public class MarketServiceImpl implements MarketService {

    private Market market;

    public MarketServiceImpl() {
        init();
    }

    private void init() {
        market = Market.builder()
                .orderBooks(new CopyOnWriteArrayList<>(
                        asList(OrderBook.builder()
                                .id(randomUUID().toString())
                                .isOpen(true)
                                .financialInstrument(FinancialInstrument.BTC)
                                .orders(new ConcurrentLinkedQueue<>(asList(Order.builder()
                                        .id(randomUUID().toString())
                                        .price(TEN)
                                        .quantity(5)
                                        .entryTime(now())
                                        .build()))
                                )
                                .build()
                        )))
                .build();
    }

    @Override
    public List<OrderBook> getOrderBooks() {
        return market.getOrderBooks();
    }

    @Override
    public OrderBook getOrderBookById(String id) {
        return market.getOrderBooks().stream().filter(orderBook -> StringUtils.equals(id, orderBook.getId())).findFirst().get();
    }

    @Override
    public OrderBook closeOrderBook(String id) {
        Optional<OrderBook> maybeOrderBook = market.getOrderBooks().stream().filter(orderBook -> StringUtils.equals(id, orderBook.getId())).findFirst();
        if (maybeOrderBook.isPresent()) {
            maybeOrderBook.get().setOpen(false);
            return maybeOrderBook.get();
        }
        throw new NoSuchElementException();
    }

    @Override
    public OrderBook openOrderBook(FinancialInstrument financialInstrument) {
        OrderBook newOrderBook = OrderBook.builder()
                .id(randomUUID().toString())
                .financialInstrument(financialInstrument)
                .isOpen(true)
                .build();
        market.getOrderBooks().add(newOrderBook);
        return newOrderBook;
    }

}
