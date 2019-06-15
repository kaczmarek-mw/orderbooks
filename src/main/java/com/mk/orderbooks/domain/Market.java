package com.mk.orderbooks.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Market {

    private List<OrderBook> orderBooks;
}
