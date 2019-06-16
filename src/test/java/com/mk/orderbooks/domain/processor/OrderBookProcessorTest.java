package com.mk.orderbooks.domain.processor;

import com.mk.orderbooks.domain.Execution;
import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.Order;
import com.mk.orderbooks.domain.OrderBook;
import com.mk.orderbooks.domain.mutable.MutableExecution;
import com.mk.orderbooks.domain.mutable.MutableOrder;
import com.mk.orderbooks.domain.mutable.MutableOrderBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

class OrderBookProcessorTest {

    private OrderBookProcessor orderBookProcessor = new OrderBookProcessor();

    private MutableOrderBook orderBook;

    private Order buildLimitOrder (int quantity, BigDecimal pricBigDecimal) {
        return Order.builder()
                .id(randomUUID().toString())
                .isMarketOrder(false)
                .price(pricBigDecimal)
                .quantity(quantity)
                .entryTime(now())
                .build();
    }

    private Order buildMarketOrder (int quantity) {
        return Order.builder()
                .id(randomUUID().toString())
                .isMarketOrder(true)
                .quantity(quantity)
                .entryTime(now())
                .build();
    }

    private Execution buildExecution (int quantity) {
        return Execution.builder()
                .id(randomUUID().toString())
                .quantity(quantity)
                .build();
    }

    @BeforeEach
    void init() {
        orderBook = OrderBook.builder()
                .id(randomUUID().toString())
                .isOpen(true)
                .financialInstrument(FinancialInstrument.BTC)
                .orders(new ConcurrentLinkedQueue<>(asList(
                        buildMarketOrder(25),
                        buildLimitOrder(5, TEN),
                        buildLimitOrder(10, TEN),
                        buildLimitOrder(10, valueOf(50L)),
                        buildLimitOrder(0, valueOf(50L))
                )))
                .executions(new ConcurrentLinkedQueue<>(singletonList(
                        buildExecution(33)
                )))
                .build().toMutable();
    }

    @Test
    void runsOrderBookExecutions() {
        // WHEN
        boolean executed = orderBookProcessor.processBook(orderBook);

        // EXPECT
        assertEquals(0, orderBookProcessor.getOpenExecutions(orderBook.getExecutions()).size());
        assertEquals(2, orderBookProcessor.getOpenOrders(orderBook.getOrders()).size());
        assertEquals(1, orderBookProcessor.getValidOrders(orderBook.getOrders(), valueOf(20L)).size());
        assertTrue(executed);
    }


    @Test
    void runsOrderBookExecutionsAndThrowsExceptionIfOrderBookNull() {

        // EXPECT
        assertThrows(IllegalStateException.class, () -> orderBookProcessor.processBook(null));
    }

    @Test
    void runsOrderBookExecutionsAndThrowsExceptionIfOrdersEmpty() {

        // GIVEN
        Execution execution = buildExecution(33);
        orderBook = OrderBook.builder()
                .id(randomUUID().toString())
                .isOpen(true)
                .financialInstrument(FinancialInstrument.BTC)
                .orders(new ConcurrentLinkedQueue<>())
                .executions(new ConcurrentLinkedQueue<>(singletonList(execution)))
                .build().toMutable();

        // EXPECT
        assertThrows(IllegalArgumentException.class, () -> orderBookProcessor.processBook(orderBook));
        assertEquals(33, execution.getQuantity());
    }

    @Test
    void runsOrderBookExecutionsAndThrowsExceptionIfExecutionsEmpty() {

        // GIVEN
        Order order = buildLimitOrder(10, TEN);
        orderBook = OrderBook.builder()
                .id(randomUUID().toString())
                .isOpen(true)
                .financialInstrument(FinancialInstrument.BTC)
                .orders(new ConcurrentLinkedQueue<>(singletonList(order)))
                .executions(new ConcurrentLinkedQueue<>())
                .build().toMutable();

        // EXPECT
        assertThrows(IllegalArgumentException.class, () -> orderBookProcessor.processBook(orderBook));
        assertEquals(10, order.getQuantity());
    }

    @Test
    void distributeExecutionBetweenFiveOrders() {
        // GIVEN

        MutableOrder outstandingOrder = buildMarketOrder(25).toMutable();
        List<MutableOrder> orders = asList(
                outstandingOrder,
                buildLimitOrder(5, TEN).toMutable(),
                buildLimitOrder(10, TEN).toMutable(),
                buildLimitOrder(10, valueOf(50L)).toMutable(),
                buildLimitOrder(0, valueOf(50L)).toMutable()
        );
        MutableExecution execution = buildExecution(33).toMutable();

        // WHEN
        orderBookProcessor.distributeExecution(orders, execution);

        // THEN
        assertEquals(2, orderBookProcessor.getOpenOrders(orders).size());
        assertEquals(0, orderBookProcessor.getOpenExecutions(singletonList(execution)).size());
        assertEquals(1, orderBookProcessor.getValidOrders(orders, execution.getPrice()).size());
        assertEquals(0, execution.getQuantity());
        assertEquals(7, outstandingOrder.getQuantity());
    }

    @Test
    void executesOrderAndBuysAllOut() {
        // GIVEN
        MutableOrder order = buildLimitOrder(5, TEN).toMutable();
        MutableExecution execution = buildExecution(33).toMutable();

        // WHEN
        orderBookProcessor.executeOrder(order, execution, 7);

        // THEN
        assertEquals(0, order.getQuantity());
        assertEquals(28, execution.getQuantity());
    }

    @Test
    void executesOrderAndQuantityRemains() {
        // GIVEN
        MutableOrder order = buildLimitOrder(15, TEN).toMutable();
        MutableExecution execution = buildExecution(4).toMutable();

        // WHEN
        orderBookProcessor.executeOrder(order, execution, 3);

        // THEN
        assertEquals(12, order.getQuantity());
        assertEquals(1, execution.getQuantity());
    }

    @Test
    void executesOrderAndThrowsException() {
        // GIVEN
        MutableOrder order = buildLimitOrder(15, TEN).toMutable();
        MutableExecution execution = buildExecution(4).toMutable();

        // EXPECT
        assertThrows(IllegalStateException.class, () -> orderBookProcessor.executeOrder(order, execution, 7));
        assertEquals(15, order.getQuantity());
        assertEquals(4, execution.getQuantity());
    }

    @Test
    void returnsRemainingOpenExecutions() {

        // GIVEN
        List<MutableExecution> executions = asList(
                buildExecution(5).toMutable(),
                buildExecution(15).toMutable(),
                buildExecution(0).toMutable()
        );

        // EXPECT
        assertEquals(2, orderBookProcessor.getOpenExecutions(executions).size());
    }

    @Test
    void returnsRemainingValidOrdersForOrderBook() {

        // EXPECT
        assertEquals(3, orderBookProcessor.getValidOrders(orderBook).size());
    }


    @Test
    void returnsRemainingValidOrders() {

        // EXPECT
        assertEquals(3, orderBookProcessor.getValidOrders(orderBook.getOrders(), valueOf(20L)).size());
        assertEquals(1, orderBookProcessor.getValidOrders(orderBook.getOrders(), valueOf(1L)).size());
        assertEquals(4, orderBookProcessor.getValidOrders(orderBook.getOrders(), valueOf(1000L)).size());
    }

    @Test
    void returnsRemainingOpenOrders() {

        // EXPECT
        assertEquals(4, orderBookProcessor.getOpenOrders(orderBook.getOrders()).size());
    }

    @Test
    void distributesQuantityAmongOrdersEvenly() {

        // EXPECT
        assertEquals(1, orderBookProcessor.getEvenDistribution(13, 9));
        assertEquals(2, orderBookProcessor.getEvenDistribution(13, 6));
        assertEquals(4, orderBookProcessor.getEvenDistribution(40, 10));
        assertEquals(5, orderBookProcessor.getEvenDistribution(15, 3));
        assertEquals(1, orderBookProcessor.getEvenDistribution(8, 8));
        assertEquals(0, orderBookProcessor.getEvenDistribution(8, 22));
    }

}