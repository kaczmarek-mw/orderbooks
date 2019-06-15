package com.mk.orderbooks.controller;

import com.mk.orderbooks.controller.request.NewOrderBookRequest;
import com.mk.orderbooks.hateoas.OrderBookResource;
import com.mk.orderbooks.hateoas.OrderBooksResource;
import com.mk.orderbooks.service.MarketService;
import com.mk.orderbooks.service.Messages;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/order-books")
@Api(value = "/order-books", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderBookController {

    private final MarketService marketService;
    private final Messages messages;

    public OrderBookController(MarketService marketService, Messages messages) {
        this.marketService = marketService;
        this.messages = messages;
    }

    @GetMapping()
    @ApiOperation(
            value = "Finds all Order Books",
            response = OrderBooksResource.class,
            responseContainer = "List",
            notes = "It fetches both open and closed books")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "security not implemented")})
    public OrderBooksResource getOrderBooks() {
        return new OrderBooksResource(marketService.getOrderBooks());
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(
            value = "Finds one Order Book by id",
            response = OrderBookResource.class,
            notes = "It fetches a single order book or throws 404 if not found")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrderBookResource getOrderBook(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String id) {
        return new OrderBookResource(marketService.getOrderBookById(id));
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(
            value = "Closes an order book",
            notes = "Closes given order book after which executions will be accepted.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Resource not found!")})
    public ResponseEntity<OrderBookResource> closeOrderBook(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String id) {
        OrderBookResource orderBookResource = new OrderBookResource(marketService.closeOrderBook(id));
        return ok().body(orderBookResource);
    }

    @PostMapping
    @ApiOperation(
            value = "Opens a new order book",
            notes = "Opens new order book for a particular financial instrument")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = OrderBooksResource.class),
            @ApiResponse(code = 400, message = "Request malformed or not consistent. Check your request body!")})
    public ResponseEntity<OrderBookResource> openOrderBook(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @RequestBody NewOrderBookRequest newOrderBookRequest) throws URISyntaxException {
        OrderBookResource orderBookResource = new OrderBookResource(marketService.openOrderBook(newOrderBookRequest.getFinancialInstrument()));
        return ResponseEntity.created(new URI(orderBookResource.getLink("self").getHref())).body(orderBookResource);
    }

}
