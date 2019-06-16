package com.mk.orderbooks.hateoas;

import com.mk.orderbooks.controller.FinancialInstrumentController;
import com.mk.orderbooks.domain.FinancialInstrument;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class FinancialInstrumentsResource extends ResourceSupport {

    private final FinancialInstrument[] financialInstruments;

    public FinancialInstrumentsResource() {
        financialInstruments = FinancialInstrument.values();
        add(linkTo(FinancialInstrumentController.class).withSelfRel());
    }
}
