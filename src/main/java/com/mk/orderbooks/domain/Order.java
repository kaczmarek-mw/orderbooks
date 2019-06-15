package com.mk.orderbooks.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
public class Order {

    private final String id;
    private final int quantity;
    private final Instant entryTime;
    private final BigDecimal price;

}
