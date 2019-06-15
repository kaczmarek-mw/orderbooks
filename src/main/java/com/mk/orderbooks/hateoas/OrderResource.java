package com.mk.orderbooks.hateoas;

import com.mk.orderbooks.controller.OrderBookController;
import com.mk.orderbooks.domain.Order;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class OrderResource extends ResourceSupport {

    private final Order order;

    public OrderResource(final String orderBookId, final Order order) {
        this.order = order;
        add(linkTo(methodOn(OrderBookController.class).getOrderBook(orderBookId)).withRel("order-book"));
        add(linkTo(methodOn(OrderBookController.class).getOrder(orderBookId, order.getId())).withSelfRel());
    }
}
