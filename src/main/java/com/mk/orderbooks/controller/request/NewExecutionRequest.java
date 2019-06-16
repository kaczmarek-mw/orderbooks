package com.mk.orderbooks.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewExecutionRequest {

    @NotNull(message = "quantity must be given!")
    private Integer quantity;

}
