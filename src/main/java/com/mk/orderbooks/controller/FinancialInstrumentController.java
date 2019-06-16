package com.mk.orderbooks.controller;

import com.mk.orderbooks.hateoas.FinancialInstrumentsResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financial-instruments")
@Api(value = "/financial-instruments", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FinancialInstrumentController {

    @GetMapping()
    @ApiOperation(
            value = "Finds all financial instruments",
            response = FinancialInstrumentsResource.class,
            responseContainer = "List",
            notes = "It fetches all supported financial instruments")
    public FinancialInstrumentsResource getFinancialInstruments() {
        return new FinancialInstrumentsResource();
    }

}
