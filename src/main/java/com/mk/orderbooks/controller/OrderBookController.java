package com.mk.orderbooks.controller;

import com.mk.orderbooks.hateoas.OrderBookResource;
import com.mk.orderbooks.hateoas.OrderBooksResource;
import com.mk.orderbooks.service.MarketService;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-books")
@Api(value = "/order-books", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderBookController {

    private final MarketService marketService;

    public OrderBookController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping()
    @ApiOperation(
            value = "Finds all Order Books",
            response = OrderBooksResource.class,
            responseContainer = "List",
            notes = "It fetches both open and closed books")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "never happens"),
            @ApiResponse(code = 403, message = "never happens"),
            @ApiResponse(code = 404, message = "never happens")})
    public OrderBooksResource getOrderBooks() {
        return new OrderBooksResource(marketService.getOrderBooks());
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(
            value = "Finds one Order Book by id",
            response = OrderBookResource.class,
            notes = "It fetches a single order book or throws 404 if not found")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "never happens"),
            @ApiResponse(code = 403, message = "never happens"),
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrderBookResource getOrderBook(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String id) {
        return new OrderBookResource(marketService.getOrderBookById(id));
    }

//    public ResponseEntity<Void> closeOrderBook ()


}
