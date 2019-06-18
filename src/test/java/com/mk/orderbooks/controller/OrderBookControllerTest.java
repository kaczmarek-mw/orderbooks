package com.mk.orderbooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.orderbooks.controller.request.NewOrderBookRequest;
import com.mk.orderbooks.domain.FinancialInstrument;
import com.mk.orderbooks.domain.OrderBook;
import com.mk.orderbooks.domain.OrderBookStatistics;
import com.mk.orderbooks.domain.mutable.MutableOrderBook;
import com.mk.orderbooks.domain.processor.OrderBookProcessor;
import com.mk.orderbooks.domain.processor.OrderBookStatisticsProcessor;
import com.mk.orderbooks.domain.processor.OrderStatisticsProcessor;
import com.mk.orderbooks.service.Messages;
import com.mk.orderbooks.service.OrderBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.mk.orderbooks.domain.FinancialInstrument.BTC;
import static com.mk.orderbooks.domain.FinancialInstrument.CHF;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderBookController.class)
class OrderBookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderBookService orderBookService;
    @MockBean
    private OrderBookProcessor orderBookProcessor;
    @MockBean
    private OrderBookStatisticsProcessor orderBookStatisticsProcessor;
    @MockBean
    private OrderStatisticsProcessor orderStatisticsProcessor;
    @MockBean
    private Messages messages;

    private ObjectMapper jackson = new ObjectMapper();

    private OrderBook newOrderBook(FinancialInstrument financialInstrument) {
        return OrderBook.builder()
                .id(randomUUID().toString())
                .financialInstrument(financialInstrument)
                .orders(emptyList())
                .executions(emptyList())
                .isOpen(true)
                .build();
    }

    @BeforeEach
    private void setupMocks() {
        when(orderBookService.getOrderBooks()).thenReturn(singletonList(newOrderBook(BTC)));
        when(orderBookService.getOrderBook(any())).thenReturn(newOrderBook(BTC));
        when(orderBookService.openOrderBook(any())).thenReturn(newOrderBook(CHF));
        when(orderBookStatisticsProcessor.collectStatistics(any(MutableOrderBook.class))).thenReturn(
                OrderBookStatistics.builder()
                        .orderCount(9)
                        .validOrderCount(9)
                        .invalidOrderCount(9)
                        .build());

    }

    @Test
    public void returnsAllOrders() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/order-books")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderBooks", hasSize(1)))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    public void returnsNewlyCreatedOrderBook() throws Exception {
        NewOrderBookRequest newOrderBookRequest = new NewOrderBookRequest();
        newOrderBookRequest.setFinancialInstrument(CHF);
        mvc.perform(MockMvcRequestBuilders
                .post("/order-books")
                .content(jackson.writeValueAsString(newOrderBookRequest))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderBook").exists())
                .andExpect(jsonPath("$.orderBook.financialInstrument").value("CHF"))
                .andExpect(jsonPath("$._links.order-books").exists())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    public void throwsHTTP400WhenBodyIsInvalid() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/order-books")
                .content("some wrong content")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void returnsStatisticsForOrderBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/order-books/someId/statistics")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statistics.orderCount").value("9"))
                .andExpect(jsonPath("$.statistics.validOrderCount").value("9"))
                .andExpect(jsonPath("$.statistics.invalidOrderCount").value("9"))
                .andExpect(jsonPath("$._links.self").exists());
    }
}