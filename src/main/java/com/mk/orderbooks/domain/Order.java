package com.mk.orderbooks.domain;

import com.mk.orderbooks.domain.mutable.MutableOrder;
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
    private final boolean isMarketOrder;
    private final BigDecimal price;

    public MutableOrder toMutable() {
        return new MutableOrder(this);
    }
}
