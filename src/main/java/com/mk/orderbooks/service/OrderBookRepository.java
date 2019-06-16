package com.mk.orderbooks.service;

import com.mk.orderbooks.domain.Execution;
import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.Order;
import com.mk.orderbooks.domain.OrderBook;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

@Service
public class OrderBookRepository {

    private final List<OrderBook> orderBooks = new CopyOnWriteArrayList<>();

    public OrderBookRepository() {
        init();
    }

    private void init() {
        orderBooks.add(OrderBook.builder()
                .id(randomUUID().toString())
                .isOpen(false)
                .financialInstrument(FinancialInstrument.BTC)
                .orders(new ConcurrentLinkedQueue<>(asList(
                        Order.builder()
                                .id(randomUUID().toString())
                                .isMarketOrder(false)
                                .price(TEN)
                                .quantity(5)
                                .entryTime(now())
                                .build(),
                        Order.builder()
                                .id(randomUUID().toString())
                                .isMarketOrder(false)
                                .price(valueOf(30L))
                                .quantity(15)
                                .entryTime(now())
                                .build(),
                        Order.builder()
                                .id(randomUUID().toString())
                                .isMarketOrder(false)
                                .price(TEN)
                                .quantity(12)
                                .entryTime(now())
                                .build(),
                        Order.builder()
                                .id(randomUUID().toString())
                                .isMarketOrder(true)
                                .quantity(15)
                                .entryTime(now())
                                .build()))
                )
                .executions(new ConcurrentLinkedQueue<>(asList(
                        Execution.builder()
                                .id(randomUUID().toString())
                                .quantity(18)
                                .build(),
                        Execution.builder()
                                .id(randomUUID().toString())
                                .quantity(4)
                                .build())))
                .build());
    }

    List<OrderBook> findAllOrderBooks() {
        return orderBooks;
    }

    Optional<OrderBook> findOrderBookById(String id) {
        return orderBooks.stream().filter(orderBook -> StringUtils.equals(id, orderBook.getId())).findFirst();
    }

    Collection<Order> findOrdersByOrderBookId(String orderBookId) {
        Optional<OrderBook> maybeOrderBook = findOrderBookById(orderBookId);
        if (maybeOrderBook.isPresent()) {
            return maybeOrderBook.get().getOrders();
        }
        throw new NoSuchElementException();
    }

    Collection<Execution> findExecutionsByOrderBookId(String orderBookId) {
        Optional<OrderBook> maybeOrderBook = findOrderBookById(orderBookId);
        if (maybeOrderBook.isPresent()) {
            return maybeOrderBook.get().getExecutions();
        }
        throw new NoSuchElementException();
    }

    Optional<Order> findOrderByOrderBookByIdAndOrderId(String orderBookId, String orderId) {
        Optional<OrderBook> maybeOrderBook = findOrderBookById(orderBookId);
        return maybeOrderBook.flatMap(orderBook -> orderBook.getOrders().stream().filter(order -> StringUtils.equals(orderId, order.getId())).findFirst());
    }

    Optional<Execution> findExecutionByOrderBookIdAndExecutionId(String orderBookId, String executionId) {
        Optional<OrderBook> maybeOrderBook = findOrderBookById(orderBookId);
        return maybeOrderBook.flatMap(orderBook -> orderBook.getExecutions().stream().filter(execution -> StringUtils.equals(executionId, execution.getId())).findFirst());
    }

    void addOrderBook(OrderBook orderBook) {
        orderBooks.add(orderBook);
    }

    void addOrder(String orderBookId, Order order) {
        Optional<OrderBook> maybeOrderBook = findOrderBookById(orderBookId);
        maybeOrderBook.ifPresent(orderBook -> orderBook.getOrders().add(order));
    }

    void addExecution(String orderBookId, Execution execution) {
        Optional<OrderBook> maybeOrderBook = findOrderBookById(orderBookId);
        maybeOrderBook.ifPresent(orderBook -> orderBook.getExecutions().add(execution));
    }
}
