package com.mk.orderbooks.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
public class OrderBookStatistics {

    private long orderCount;
    private long invalidOrderCount;
    private long validOrderCount;
    private long openOrderCount;
    private long closedOrderCount;
    private long openExecutionsCount;
    private long closedExecutionsCount;
    private int demand;
    private int invalidDemand;
    private int validDemand;
    private int biggestOrder;
    private int smallestOrder;
    private Instant earliestOrderEntry;
    private Instant lastOrderEntry;
    private Map<BigDecimal, IntSummaryStatistics> limitBreakdown;
    private int executionQuantity;
    private Set<BigDecimal> executionPrices;

}
