package com.mk.orderbooks.controller.request;

import com.mk.orderbooks.controller.validation.MarketOrderConstraint;
import com.mk.orderbooks.controller.validation.MoreThanZero;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@MarketOrderConstraint
public class NewOrderRequest {

    @NotNull(message = "quantity must be given!")
    @MoreThanZero(message = "quantity must be more than zero!")
    private Integer quantity;
    private boolean isMarketOrder;
    private BigDecimal price;

}
