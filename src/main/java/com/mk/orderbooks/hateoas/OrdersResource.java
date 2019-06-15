package com.mk.orderbooks.hateoas;

import com.mk.orderbooks.controller.OrderBookController;
import com.mk.orderbooks.domain.Order;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class OrdersResource extends ResourceSupport {

    private final Collection<OrderResource> orders;

    public OrdersResource(final String orderBookId, final Collection<Order> orders) {
        if (orders != null) {
            this.orders = orders
                    .stream()
                    .map(order -> new OrderResource(orderBookId, order))
                    .collect(toList());
        } else {
            this.orders = emptyList();
        }

        add(linkTo(methodOn(OrderBookController.class).getOrderBook(orderBookId)).withRel("order-book"));
        add(linkTo(methodOn(OrderBookController.class).getOrders(orderBookId)).withSelfRel());
    }
}
