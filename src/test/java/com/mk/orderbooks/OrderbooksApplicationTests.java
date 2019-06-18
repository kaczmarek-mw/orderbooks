package com.mk.orderbooks;

import com.mk.orderbooks.controller.FinancialInstrumentController;
import com.mk.orderbooks.controller.GracefulShutdownTestController;
import com.mk.orderbooks.controller.OrderBookController;
import com.mk.orderbooks.service.Messages;
import com.mk.orderbooks.service.OrderBookRepository;
import com.mk.orderbooks.service.OrderBookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderbooksApplicationTests {

    @Autowired
    private FinancialInstrumentController financialInstrumentController;
    @Autowired
    private GracefulShutdownTestController gracefulShutdownTestController;
    @Autowired
    private OrderBookController orderBookController;
    @Autowired
    private Messages messages;
    @Autowired
    private OrderBookService orderBookService;
    @Autowired
    private OrderBookRepository orderBookRepository;

    @Test
    public void contextLoads() {
        assertThat(financialInstrumentController).isNotNull();
        assertThat(gracefulShutdownTestController).isNotNull();
        assertThat(orderBookController).isNotNull();
        assertThat(messages).isNotNull();
        assertThat(orderBookService).isNotNull();
        assertThat(orderBookRepository).isNotNull();
    }

}
