package com.mk.orderbooks.controller;

import com.mk.orderbooks.hateoas.OrderBookResource;
import com.mk.orderbooks.hateoas.OrderBooksResource;
import com.mk.orderbooks.service.MarketService;
import com.mk.orderbooks.service.Messages;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mk.orderbooks.junk.MessageKeys.MESSAGE_RESOURCE_NOT_UPDATED;
import static com.mk.orderbooks.junk.MessageKeys.MESSAGE_RESOURCE_UPDATED;
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
            @ApiResponse(code = 401, message = "security not implemented"),
            @ApiResponse(code = 403, message = "security not implemented"),
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
            @ApiResponse(code = 401, message = "security not implemented"),
            @ApiResponse(code = 403, message = "security not implemented"),
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
            @ApiResponse(code = 200, message = "Resource was updated || Resource was not updated"),
            @ApiResponse(code = 401, message = "security not implemented"),
            @ApiResponse(code = 403, message = "security not implemented"),
            @ApiResponse(code = 404, message = "Resource not found!")})
    public ResponseEntity<String> closeOrderBook(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String id) {
        boolean closeResult = marketService.closeOrderBook(id);
        return ok().body(
                closeResult ? messages.getMessage(MESSAGE_RESOURCE_UPDATED) : messages.getMessage(MESSAGE_RESOURCE_NOT_UPDATED));
    }


}
