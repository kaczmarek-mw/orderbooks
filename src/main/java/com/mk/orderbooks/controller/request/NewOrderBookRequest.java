package com.mk.orderbooks.controller.request;

import com.mk.orderbooks.domain.FinancialInstrument;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewOrderBookRequest {

    @NotNull(message = "financialInstrument must be given!")
    private FinancialInstrument financialInstrument;
}
