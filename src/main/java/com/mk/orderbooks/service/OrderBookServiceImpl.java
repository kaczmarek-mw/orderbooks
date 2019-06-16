package com.mk.orderbooks.service;

import com.mk.orderbooks.domain.Execution;
import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.Order;
import com.mk.orderbooks.domain.OrderBook;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.mk.orderbooks.junk.MessageKeys.ERROR_ORDER_BOOK_IS_CLOSED;
import static com.mk.orderbooks.junk.MessageKeys.ERROR_ORDER_BOOK_IS_OPEN;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Service
public class OrderBookServiceImpl implements OrderBookService {

    private OrderBookRepository orderBookRepository;
    private final Messages messages;

    public OrderBookServiceImpl(OrderBookRepository orderBookRepository, Messages messages) {
        this.orderBookRepository = orderBookRepository;
        this.messages = messages;
    }

    @Override
    public List<OrderBook> getOrderBooks() {
        return orderBookRepository.findAllOrderBooks();
    }

    @Override
    public OrderBook getOrderBook(String id) {
        return orderBookRepository.findOrderBookById(id).get();
    }

    @Override
    public OrderBook closeOrderBook(String id) {
        Optional<OrderBook> maybeOrderBook = orderBookRepository.findOrderBookById(id);
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
        orderBookRepository.addOrderBook(newOrderBook);
        return newOrderBook;
    }

    @Override
    public Order addOrder(String orderBookId, int quantity, boolean isMarketOrder, BigDecimal price) {
        Optional<OrderBook> maybeOrderBook = orderBookRepository.findOrderBookById(orderBookId);
        if (maybeOrderBook.isPresent()) {
            OrderBook orderBook = maybeOrderBook.get();
            if (orderBook.isOpen()) {
                Order order = Order.builder()
                        .id(randomUUID().toString())
                        .price(price)
                        .isMarketOrder(isMarketOrder)
                        .quantity(quantity)
                        .entryTime(now())
                        .build();
                orderBookRepository.addOrder(orderBook.getId(), order);
                return order;
            } else {
                throw new IllegalStateException(messages.getMessage(ERROR_ORDER_BOOK_IS_CLOSED));
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public Collection<Order> getOrders(String orderBookId) {
        return orderBookRepository.findOrdersByOrderBookId(orderBookId);
    }

    @Override
    public Order getOrder(String orderBookId, String orderId) {
        Optional<Order> maybeOrder = orderBookRepository.findOrderByOrderBookByIdAndOrderId(orderBookId, orderId);
        if (maybeOrder.isPresent()) {
            return maybeOrder.get();
        }
        throw new NoSuchElementException();
    }

    @Override
    public Execution addExecution(String orderBookId, int quantity) {
        Optional<OrderBook> maybeOrderBook = orderBookRepository.findOrderBookById(orderBookId);
        if (maybeOrderBook.isPresent()) {
            OrderBook orderBook = maybeOrderBook.get();
            if (!orderBook.isOpen()) {
                Execution execution = Execution.builder()
                        .id(randomUUID().toString())
                        .quantity(quantity)
                        .build();
                orderBookRepository.addExecution(orderBook.getId(), execution);
                return execution;
            } else {
                throw new IllegalStateException(messages.getMessage(ERROR_ORDER_BOOK_IS_OPEN));
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public Collection<Execution> getExecutions(String orderBookId) {
        return orderBookRepository.findExecutionsByOrderBookId(orderBookId);
    }

    @Override
    public Execution getExecution(String orderBookId, String executionId) {
        Optional<Execution> maybeExecution = orderBookRepository.findExecutionByOrderBookIdAndExecutionId(orderBookId, executionId);
        if (maybeExecution.isPresent()) {
            return maybeExecution.get();
        }
        throw new NoSuchElementException();
    }
}
