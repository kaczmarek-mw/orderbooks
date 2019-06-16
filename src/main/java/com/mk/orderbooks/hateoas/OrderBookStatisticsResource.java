package com.mk.orderbooks.hateoas;

import com.mk.orderbooks.controller.OrderBookController;
import com.mk.orderbooks.domain.OrderBookStatistics;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class OrderBookStatisticsResource extends ResourceSupport {

    OrderBookStatistics statistics;

    public OrderBookStatisticsResource(final String orderBookId, OrderBookStatistics statistics) {
        this.statistics = statistics;
        add(linkTo(methodOn(OrderBookController.class).getOrderBook(orderBookId)).withRel("order-book"));
        add(linkTo(methodOn(OrderBookController.class).getOrderBookStatistics(orderBookId)).withSelfRel());
    }
}
