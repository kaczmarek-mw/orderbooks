package com.mk.orderbooks.hateoas;

import com.mk.orderbooks.controller.OrderBookController;
import com.mk.orderbooks.domain.OrderBook;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class OrderBookResource extends ResourceSupport {

    private final OrderBook orderBook;

    public OrderBookResource(final OrderBook orderBook) {
        this.orderBook = orderBook;
        add(linkTo(OrderBookController.class).withRel("order-books"));
        add(linkTo(methodOn(OrderBookController.class).getOrderBook(orderBook.getId())).withSelfRel());
    }
}
