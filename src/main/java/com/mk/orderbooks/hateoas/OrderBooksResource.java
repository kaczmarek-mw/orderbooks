package com.mk.orderbooks.hateoas;

import com.mk.orderbooks.controller.OrderBookController;
import com.mk.orderbooks.domain.OrderBook;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class OrderBooksResource extends ResourceSupport {

    private final List<OrderBookResource> orderBooks;

    public OrderBooksResource(final List<OrderBook> orderBooks) {
        this.orderBooks = emptyIfNull(orderBooks)
                .stream()
                .map(orderBook -> new OrderBookResource(orderBook))
                .collect(toList());
        add(linkTo(OrderBookController.class).withSelfRel());
    }
}
