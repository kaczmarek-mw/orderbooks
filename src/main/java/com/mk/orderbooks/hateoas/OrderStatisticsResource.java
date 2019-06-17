package com.mk.orderbooks.hateoas;

import com.mk.orderbooks.controller.OrderBookController;
import com.mk.orderbooks.domain.OrderStatistics;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class OrderStatisticsResource extends ResourceSupport {

    OrderStatistics statistics;

    public OrderStatisticsResource(final String orderBookId, final String orderId, OrderStatistics statistics) {
        this.statistics = statistics;
        add(linkTo(methodOn(OrderBookController.class).getOrderBook(orderBookId)).withRel("order-book"));
        add(linkTo(methodOn(OrderBookController.class).getOrder(orderBookId, orderId)).withRel("order"));
        add(linkTo(methodOn(OrderBookController.class).getOrderStatistics(orderBookId, orderId)).withSelfRel());
    }
}
