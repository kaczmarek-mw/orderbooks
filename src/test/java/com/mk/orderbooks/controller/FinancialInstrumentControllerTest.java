package com.mk.orderbooks.controller;

import com.mk.orderbooks.service.Messages;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FinancialInstrumentController.class)
class FinancialInstrumentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Messages messages;

    @Test
    public void testFinancialInstrumentsEndpoint() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/financial-instruments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.financialInstruments").exists())
                .andExpect(jsonPath("$.financialInstruments[0]").value("BTC"))
                .andExpect(jsonPath("$.financialInstruments", hasSize(5)))
                .andExpect(jsonPath("$._links.self").exists());
    }
}