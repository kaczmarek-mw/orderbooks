package com.mk.orderbooks.controller;

import com.mk.orderbooks.controller.request.NewExecutionRequest;
import com.mk.orderbooks.controller.request.NewOrderBookRequest;
import com.mk.orderbooks.controller.request.NewOrderRequest;
import com.mk.orderbooks.domain.mutable.MutableOrderBook;
import com.mk.orderbooks.domain.processor.OrderBookProcessor;
import com.mk.orderbooks.domain.processor.OrderBookStatisticsProcessor;
import com.mk.orderbooks.domain.processor.OrderStatisticsProcessor;
import com.mk.orderbooks.hateoas.*;
import com.mk.orderbooks.service.OrderBookService;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/order-books")
@Api(value = "/order-books", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderBookController {

    private final OrderBookService orderBookService;
    private final OrderBookProcessor orderBookProcessor;
    private final OrderBookStatisticsProcessor orderBookStatisticsProcessor;
    private final OrderStatisticsProcessor orderStatisticsProcessor;

    public OrderBookController(
            OrderBookService orderBookService,
            OrderBookProcessor orderBookProcessor,
            OrderBookStatisticsProcessor orderBookStatisticsProcessor,
            OrderStatisticsProcessor orderStatisticsProcessor) {
        this.orderBookService = orderBookService;
        this.orderBookProcessor = orderBookProcessor;
        this.orderBookStatisticsProcessor = orderBookStatisticsProcessor;
        this.orderStatisticsProcessor = orderStatisticsProcessor;
    }

    @GetMapping()
    @ApiOperation(
            value = "Finds all Order Books",
            response = OrderBooksResource.class,
            responseContainer = "List",
            notes = "It fetches both open and closed books")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrderBooksResource getOrderBooks() {
        return new OrderBooksResource(orderBookService.getOrderBooks());
    }

    @GetMapping(value = "/{orderBookId}")
    @ApiOperation(
            value = "Finds one Order Book by id",
            response = OrderBookResource.class,
            notes = "It fetches a single order book.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrderBookResource getOrderBook(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId) {
        return new OrderBookResource(orderBookService.getOrderBook(orderBookId));
    }

    @PutMapping(value = "/{orderBookId}")
    @ApiOperation(
            value = "Closes an order book",
            notes = "Closes given order book after which executions will be accepted.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Resource not found!")})
    public ResponseEntity<OrderBookResource> closeOrderBook(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId) {
        OrderBookResource orderBookResource = new OrderBookResource(orderBookService.closeOrderBook(orderBookId));
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
            @Valid @RequestBody NewOrderBookRequest newOrderBookRequest) throws URISyntaxException {
        OrderBookResource orderBookResource = new OrderBookResource(orderBookService.openOrderBook(newOrderBookRequest.getFinancialInstrument()));
        return created(new URI(orderBookResource.getLink("self").getHref())).body(orderBookResource);
    }

    @GetMapping(value = "/{orderBookId}/statistics")
    @ApiOperation(
            value = "Finds list of all orders for given book")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrderBookStatisticsResource getOrderBookStatistics(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId) {
        return new OrderBookStatisticsResource(orderBookId, orderBookStatisticsProcessor.collectStatistics(orderBookService.getOrderBook(orderBookId).toMutable()));
    }

    @GetMapping(value = "/{orderBookId}/statistics-after-execution")
    @ApiOperation(
            value = "Runs all stored executions against all stored orders and returns statistics after execution.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrderBookStatisticsResource execute(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId) {
        MutableOrderBook mutableOrderBook = orderBookService.getOrderBook(orderBookId).toMutable();
        orderBookProcessor.processBook(mutableOrderBook);
        return new OrderBookStatisticsResource(orderBookId, orderBookStatisticsProcessor.collectStatistics(mutableOrderBook));
    }

    @GetMapping(value = "/{orderBookId}/orders")
    @ApiOperation(
            value = "Finds list of all orders for given book")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrdersResource getOrders(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId) {
        return new OrdersResource(orderBookId, orderBookService.getOrders(orderBookId));
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
        return new OrderResource(orderBookId, orderBookService.getOrder(orderBookId, orderId));
    }

    @GetMapping(value = "/{orderBookId}/orders/{orderId}/statistics-after-execution")
    @ApiOperation(
            value = "Finds particular order for given order book")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public OrderStatisticsResource getOrderStatistics(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId,
            @ApiParam(value = "Unique ID of an order ", required = true)
            @PathVariable String orderId) {
        MutableOrderBook mutableOrderBook = orderBookService.getOrderBook(orderBookId).toMutable();
        orderBookProcessor.processBook(mutableOrderBook);
        return new OrderStatisticsResource(orderBookId, orderId, orderStatisticsProcessor.collectStatistics(mutableOrderBook, orderBookService.getOrder(orderBookId, orderId)));
    }

    @PostMapping(value = "/{orderBookId}/orders")
    @ApiOperation(
            value = "Adds new order to the order book",
            notes = "New order can be added to open order book only.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = OrderResource.class),
            @ApiResponse(code = 400, message = "Validation message"),
            @ApiResponse(code = 412, message = "This order book is closed!")})
    public ResponseEntity<OrderResource> addOrder(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId,
            @ApiParam(value = "New order data", required = true)
            @Valid @RequestBody NewOrderRequest newOrderRequest) throws URISyntaxException {
        OrderResource orderResource = new OrderResource(orderBookId,
                orderBookService.addOrder(
                        orderBookId,
                        newOrderRequest.getQuantity(),
                        newOrderRequest.isMarketOrder(),
                        newOrderRequest.getPrice()));
        return created(new URI(orderResource.getLink("self").getHref())).body(orderResource);
    }

    @PostMapping(value = "/{orderBookId}/executions")
    @ApiOperation(
            value = "Adds new execution to the order book",
            notes = "New execution can be added to closed order book only.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ExecutionResource.class),
            @ApiResponse(code = 400, message = "Validation message"),
            @ApiResponse(code = 412, message = "This order book is open!")})
    public ResponseEntity<ExecutionResource> addExecution(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId,
            @ApiParam(value = "New execution data", required = true)
            @Valid @RequestBody NewExecutionRequest newExecutionRequest) throws URISyntaxException {
        ExecutionResource executionResource = new ExecutionResource(orderBookId,
                orderBookService.addExecution(
                        orderBookId,
                        newExecutionRequest.getQuantity()));
        return created(new URI(executionResource.getLink("self").getHref())).body(executionResource);
    }

    @GetMapping(value = "/{orderBookId}/executions")
    @ApiOperation(
            value = "Finds list of all executions for given book")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public ExecutionsResource getExecutions(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId) {
        return new ExecutionsResource(orderBookId, orderBookService.getExecutions(orderBookId));
    }

    @GetMapping(value = "/{orderBookId}/executions/{executionId}")
    @ApiOperation(
            value = "Finds particular execution for given order book")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Resource not found!")})
    public ExecutionResource getExecution(
            @ApiParam(value = "Unique ID of an order book", required = true)
            @PathVariable String orderBookId,
            @ApiParam(value = "Unique ID of an execution ", required = true)
            @PathVariable String executionId) {
        return new ExecutionResource(orderBookId, orderBookService.getExecution(orderBookId, executionId));
    }

}
