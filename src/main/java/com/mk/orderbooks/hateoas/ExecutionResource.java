package com.mk.orderbooks.hateoas;

import com.mk.orderbooks.controller.OrderBookController;
import com.mk.orderbooks.domain.Execution;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class ExecutionResource extends ResourceSupport {

    private final Execution execution;

    public ExecutionResource(final String orderBookId, final Execution execution) {
        this.execution = execution;
        add(linkTo(methodOn(OrderBookController.class).getOrderBook(orderBookId)).withRel("order-book"));
        add(linkTo(methodOn(OrderBookController.class).getExecution(orderBookId, execution.getId())).withSelfRel());
    }
}
