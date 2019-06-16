package com.mk.orderbooks.controller.request;

import com.mk.orderbooks.controller.validation.MoreThanZero;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewExecutionRequest {

    @NotNull(message = "quantity must be given!")
    @MoreThanZero(message = "quantity must be more than zero!")
    private Integer quantity;

}
