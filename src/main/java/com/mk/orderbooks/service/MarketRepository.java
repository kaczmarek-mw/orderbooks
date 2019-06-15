package com.mk.orderbooks.service;

import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.Market;
import com.mk.orderbooks.domain.Order;
import com.mk.orderbooks.domain.OrderBook;
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
public class MarketRepository {

    private Market market;

    public MarketRepository() {
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
                                        .isMarketOrder(false)
                                        .price(TEN)
                                        .quantity(5)
                                        .entryTime(now())
                                        .build()))
                                )
                                .build()
                        )))
                .build();
    }

    List<OrderBook> findAllOrderBooks() {
        return market.getOrderBooks();
    }

    Optional<OrderBook> findOrderBookById(String id) {
        return market.getOrderBooks().stream().filter(orderBook -> StringUtils.equals(id, orderBook.getId())).findFirst();
    }

    Optional<Order> findOrderByOrderBookByIdAndOrderId(String orderBookId, String orderId) {
        Optional<OrderBook> maybeOrderBook = findOrderBookById(orderBookId);
        if(maybeOrderBook.isPresent()) {
            return maybeOrderBook.get().getOrders().stream().filter(order-> StringUtils.equals(orderId, order.getId())).findFirst();
        }
        throw new NoSuchElementException();
    }

    void addOrderBook(OrderBook orderBook) {
        market.getOrderBooks().add(orderBook);
    }

    void addOrder(String orderBookId, Order order) {
        Optional<OrderBook> maybeOrderBook = findOrderBookById(orderBookId);
        maybeOrderBook.ifPresent(orderBook -> orderBook.getOrders().add(order));
    }
}
