package com.mk.orderbooks.hateoas;

import com.mk.orderbooks.controller.OrderBookController;
import com.mk.orderbooks.domain.Execution;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class ExecutionsResource extends ResourceSupport {

    private final Collection<ExecutionResource> executions;

    public ExecutionsResource(final String orderBookId, final Collection<Execution> executions) {
        if (executions != null) {
            this.executions = executions
                    .stream()
                    .map(order -> new ExecutionResource(orderBookId, order))
                    .collect(toList());
        } else {
            this.executions = emptyList();
        }

        add(linkTo(methodOn(OrderBookController.class).getOrderBook(orderBookId)).withRel("order-book"));
        add(linkTo(methodOn(OrderBookController.class).getExecutions(orderBookId)).withSelfRel());
    }
}
