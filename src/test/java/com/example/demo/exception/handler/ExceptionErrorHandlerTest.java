package com.example.demo.exception.handler;

import com.example.demo.controller.MainController;
import com.example.demo.exception.NoTickerHistoryElementException;
import com.example.demo.exception.NotEnoughTickerHistoryException;
import com.example.demo.exception.ResponseErrorException;
import com.example.demo.exception.TickerNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
public class ExceptionErrorHandlerTest {

    MockMvc mockMvc;

    @Mock
    MainController mainController;

    private String tickerSymbol;

    @Before
    public void setup() {
        tickerSymbol = "AAPL";
        mockMvc = MockMvcBuilders
                .standaloneSetup(mainController)
                .setControllerAdvice(new ExceptionErrorHandler()).build();
    }

    @Test
    public void TickerNotFoundExceptionHandlerTest() throws Exception {
        TickerNotFoundException e = new TickerNotFoundException();
        when(mainController.postTickerData(tickerSymbol)).thenThrow(e);
        mockMvc.perform(post("/postTickerData")
                .param("tickerSymbol", tickerSymbol))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("errMessage", e))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void NotEnoughTickerHistoryExceptionHandlerTest() throws Exception {
        NotEnoughTickerHistoryException e = new NotEnoughTickerHistoryException();

        when(mainController.postTickerData(tickerSymbol)).thenThrow(e);
        mockMvc.perform(post("/postTickerData")
                .param("tickerSymbol", tickerSymbol))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("errMessage", e))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void ResponseErrorExceptionHandlerTest() throws Exception {
        ResponseErrorException e = new ResponseErrorException(HttpStatus.NOT_FOUND + " : ");
        when(mainController.postTickerData(tickerSymbol)).thenThrow(e);
        mockMvc.perform(post("/postTickerData")
                .param("tickerSymbol", tickerSymbol))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("errMessage", e))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void NoSuchElementExceptionHandlerTest() throws Exception {
        NoTickerHistoryElementException e = new NoTickerHistoryElementException();
        when(mainController.getTickerData(tickerSymbol)).thenThrow(e);
        mockMvc.perform(get("/getTickerData/" + tickerSymbol))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("errMessage", e))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
