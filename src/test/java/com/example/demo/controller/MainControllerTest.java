package com.example.demo.controller;

import com.example.demo.model.TickerHistory;
import com.example.demo.service.TickerHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TickerHistoryService tickerHistoryService;

    @Test
    void postTickerDataTest() throws Exception {
        String tickerSymbol = "AAPL";
        mockMvc.perform(post("/postTickerData")
                .param("tickerSymbol", tickerSymbol))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/getTickerData/" + tickerSymbol))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void getTickerDatatTest() throws Exception {
        String tickerSymbol = "AAPL";
        TickerHistory tickerHistory = new TickerHistory(tickerSymbol, 0, 0, 0);
        when(tickerHistoryService.getTickerDataInfo(tickerSymbol))
                .thenReturn(tickerHistory);

        mockMvc.perform(get("/getTickerData/" + tickerSymbol))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("tickerCostInfo", tickerHistory))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
