package com.mk.orderbooks.controller.request;

import com.mk.orderbooks.domain.FinancialInstrument;
import lombok.Data;

@Data
public class NewOrderBookRequest {

    private FinancialInstrument financialInstrument;
}
