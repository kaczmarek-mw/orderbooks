package com.mk.orderbooks.controller;

import com.mk.orderbooks.domain.OrderBook;
import com.mk.orderbooks.service.MarketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order-books")
public class OrderBookController {

    private final MarketService marketService;

    public OrderBookController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping()
    public List<OrderBook> getOrderBooks() {
        return marketService.getOrderBooks();
    }

    @GetMapping(value = "/{id}")
    public OrderBook getOrderBook(@PathVariable String id) {
        return marketService.getOrderBookById(id);
    }
}
