package com.example.demo.service;

import com.example.demo.exception.NotEnoughTickerHistoryException;
import com.example.demo.exception.ResponseErrorException;
import com.example.demo.exception.TickerNotFoundException;
import com.example.demo.model.TickerHistory;
import com.example.demo.model.stock.api.finnhub.candles.TickerHistoryResponse;
import com.example.demo.repository.TickerHistoryRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TickerHistoryServiceTest {

    @Mock
    TickerHistoryRepository tickerHistoryRepository;

    @Mock
    StockApiService stockApiService;

    @InjectMocks
    TickerHistoryService tickerHistoryService;

    private static final double DELTA = 1e-15;
    private String tickerSymbol;
    private Timestamp today;

    @Before
    public void setup() {
        tickerSymbol = "AAPL";
        today = Timestamp.valueOf(LocalDate.now().atStartOfDay());
    }

    @Test(expected = ResponseErrorException.class)
    public void containsTickerHistoryInfo_withEmptyData_shouldThrowResponseErrorException() {
        when(tickerHistoryRepository.findBySymbolAndDate(tickerSymbol, today)).thenReturn(Optional.empty());
        when(stockApiService.getTickerHistory(tickerSymbol))
                .thenThrow(new ResponseErrorException("Test"));
        tickerHistoryService.postTickerDataInfo(tickerSymbol);
    }

    @Test(expected = TickerNotFoundException.class)
    public void findTickerHistoryInfo_withNoData_shouldThrowTickerNotFoundException() {
        TickerHistoryResponse tickerHistoryResponse = new TickerHistoryResponse();
        tickerHistoryResponse.setStatus("no_data");
        tickerHistoryService.findAverageTickerPrice(tickerSymbol, tickerHistoryResponse);
    }

    @Test(expected = NotEnoughTickerHistoryException.class)
    public void findTickerHistoryInfo_withEmptyData_shouldThrowNotEnoughTickerHistoryException() {
        TickerHistoryResponse tickerHistoryResponse = new TickerHistoryResponse();
        tickerHistoryResponse.setStatus("ok");
        tickerHistoryResponse.setHigh(Collections.emptyList());
        tickerHistoryService.findAverageTickerPrice(tickerSymbol, tickerHistoryResponse);
    }

    @Test
    public void findTickerHistoryInfo_withData_shouldThrowNotEnoughTickerHistoryException() {
        TickerHistoryResponse tickerHistoryResponse = new TickerHistoryResponse();
        tickerHistoryResponse.setStatus("ok");
        tickerHistoryResponse.setHigh(List.of(3f, 3f, 3f, 3f));
        tickerHistoryResponse.setLow(List.of(1f, 1f, 1f, 1f));
        TickerHistory tickerHistory = tickerHistoryService.findAverageTickerPrice(tickerSymbol, tickerHistoryResponse);
        Assert.assertEquals(2f, tickerHistory.getAvgPrice(), DELTA);
    }

    @Test
    public void getTickerHistory_withOptionalData_shouldNotThrowNoSuchElementException() {
        TickerHistory expected = new TickerHistory(tickerSymbol, System.currentTimeMillis());
        when(tickerHistoryRepository.findBySymbolAndDate(tickerSymbol, today))
                .thenReturn(Optional.of(expected));

        TickerHistory actual = tickerHistoryService.getTickerDataInfo(tickerSymbol);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NotEnoughTickerHistoryException.class)
    public void getTickerHistory_withNull_shouldThrowNoSuchElementException() {
        when(tickerHistoryRepository.findBySymbolAndDate(tickerSymbol, today))
                .thenReturn(Optional.empty());
        tickerHistoryService.getTickerDataInfo(tickerSymbol);
    }
}
