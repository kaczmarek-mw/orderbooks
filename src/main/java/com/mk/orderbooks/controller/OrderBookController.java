package com.mk.orderbooks.controller;

import com.mk.orderbooks.controller.request.NewOrderBookRequest;
import com.mk.orderbooks.controller.request.NewOrderRequest;
import com.mk.orderbooks.hateoas.OrderBookResource;
import com.mk.orderbooks.hateoas.OrderBooksResource;
import com.mk.orderbooks.hateoas.OrderResource;
import com.mk.orderbooks.hateoas.OrdersResource;
import com.mk.orderbooks.service.MarketService;
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
            @ApiResponse(code = 404, message = "security not implemented")})
    public OrderBooksResource getOrderBooks() {
        return new OrderBooksResource(marketService.getOrderBooks());
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(
            value = "Finds one Order Book by id",
            response = OrderBookResource.class,
            notes = "It fetches a single order book.")
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
            @ApiParam(value = "New order book data", required = true)
            @RequestBody NewOrderBookRequest newOrderBookRequest) throws URISyntaxException {
        OrderBookResource orderBookResource = new OrderBookResource(marketService.openOrderBook(newOrderBookRequest.getFinancialInstrument()));
        return ResponseEntity.created(new URI(orderBookResource.getLink("self").getHref())).body(orderBookResource);
    }

    @GetMapping(value = "/{orderBookId}/orders")
    @ApiOperation(
            value = "Finds list of all orders for given book")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrdersResource getOrders(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId) {
        return new OrdersResource(orderBookId, marketService.getOrdersByOrderBookId(orderBookId));
    }

    @GetMapping(value = "/{orderBookId}/orders/{orderId}")
    @ApiOperation(
            value = "Finds particular order for given order book")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrderResource getOrder(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId,
            @ApiParam(value = "Unique ID of an order ", required = true)
            @PathVariable String orderId) {
        return new OrderResource(orderBookId, marketService.getOrderByOrderBookIdAndOrderId(orderBookId, orderId));
    }

    @PostMapping(value = "/{orderBookId}/orders")
    @ApiOperation(
            value = "Adds new order to the order book",
            notes = "New order can be added to open order book only.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = NewOrderRequest.class),
            @ApiResponse(code = 412, message = "This order book is closed!")})
    public ResponseEntity<OrderResource> addOrder(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId,
            @ApiParam(value = "New order data", required = true)
            @RequestBody NewOrderRequest newOrderRequest) throws URISyntaxException {
        OrderResource orderBookResource = new OrderResource(orderBookId, marketService.addOrder(orderBookId, newOrderRequest.getQuantity(), newOrderRequest.getPrice()));
        return ResponseEntity.created(new URI(orderBookResource.getLink("self").getHref())).body(orderBookResource);
    }


}
