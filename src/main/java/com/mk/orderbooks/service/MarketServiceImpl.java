package com.mk.orderbooks.service;

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
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Service
public class MarketServiceImpl implements MarketService {

    private MarketRepository marketRepository;
    private final Messages messages;

    public MarketServiceImpl(MarketRepository marketRepository, Messages messages) {
        this.marketRepository = marketRepository;
        this.messages = messages;
    }

    @Override
    public List<OrderBook> getOrderBooks() {
        return marketRepository.findAllOrderBooks();
    }

    @Override
    public OrderBook getOrderBookById(String id) {
        return marketRepository.findOrderBookById(id).get();
    }

    @Override
    public OrderBook closeOrderBook(String id) {
        Optional<OrderBook> maybeOrderBook = marketRepository.findOrderBookById(id);
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
        marketRepository.addOrderBook(newOrderBook);
        return newOrderBook;
    }

    @Override
    public Order addOrder(String orderBookId, int quantity, boolean isMarketOrder, BigDecimal price) {
        Optional<OrderBook> maybeOrderBook = marketRepository.findOrderBookById(orderBookId);
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
                marketRepository.addOrder(orderBook.getId(), order);
                return order;
            } else {
                throw new IllegalStateException(messages.getMessage(ERROR_ORDER_BOOK_IS_CLOSED));
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public Collection<Order> getOrdersByOrderBookId(String orderBookId) {
        Optional<OrderBook> maybeOrderBook = marketRepository.findOrderBookById(orderBookId);
        if (maybeOrderBook.isPresent()) {
            return maybeOrderBook.get().getOrders();
        }
        throw new NoSuchElementException();
    }

    @Override
    public Order getOrderByOrderBookIdAndOrderId(String orderBookId, String orderId) {
        Optional<Order> maybeOrder = marketRepository.findOrderByOrderBookByIdAndOrderId(orderBookId, orderId);
        if (maybeOrder.isPresent()) {
            return maybeOrder.get();
        }
        throw new NoSuchElementException();
    }


}
