package com.example.demo.service;

import com.example.demo.exception.ResponseErrorException;
import com.example.demo.model.stock.api.finnhub.candles.TickerHistoryResponse;
import com.example.demo.model.stock.api.finnhub.quote.TickerRealTimeInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@PropertySource("classpath:values.properties")
public class StockApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockApiService stockApiService;

    private String tickerSymbol;
    private URI TickerHistoryURI;
    private URI TickerQuoteURI;


    @Value("${apiKey}")
    private String apiKey;

    @Before
    public void setup() {
        tickerSymbol = "AAPL";
        long currentTimeMillis = System.currentTimeMillis(), sixtyDayByMillis = 5184000L;
        long end = currentTimeMillis / 1000;
        long start = end - sixtyDayByMillis;
        TickerHistoryURI = URI.create("https://finnhub.io/api/v1/stock/candle?symbol="
                + tickerSymbol + "&resolution=D&from=" + start + "&to=" + end + "&token=" + apiKey);
        TickerQuoteURI = URI.create("https://finnhub.io/api/v1/quote?symbol=" + tickerSymbol + "&token=" + apiKey);
    }

    @Test
    public void tickerHistoryResponse_getBodyTest(){
        TickerHistoryResponse expectedTickerHistoryResponse = new TickerHistoryResponse();
        when(restTemplate.getForEntity(TickerHistoryURI, TickerHistoryResponse.class))
                .thenReturn(new ResponseEntity<>(expectedTickerHistoryResponse, HttpStatus.OK));

        TickerHistoryResponse actualTickerHistoryResponse = stockApiService.getTickerHistory(tickerSymbol);

        Assert.assertEquals(expectedTickerHistoryResponse, actualTickerHistoryResponse);
    }

    @Test(expected = ResponseErrorException.class)
    public void tickerHistoryResponse_responseErrorExceptionTestWithBadRequest() {
        TickerHistoryResponse tickerHistoryResponse = new TickerHistoryResponse();
        when(restTemplate.getForEntity(TickerHistoryURI, TickerHistoryResponse.class))
                .thenReturn(new ResponseEntity<>(tickerHistoryResponse, HttpStatus.BAD_REQUEST));
        stockApiService.getTickerHistory(tickerSymbol);
    }

    @Test
    public void tickerQuoteResponse_getBodyTest(){
        TickerRealTimeInfo expectedTickerRealTimeInfo = new TickerRealTimeInfo();
        when(restTemplate.getForEntity(TickerQuoteURI, TickerRealTimeInfo.class))
                .thenReturn(new ResponseEntity<>(expectedTickerRealTimeInfo, HttpStatus.OK));

        TickerRealTimeInfo actualTickerRealTimeInfo = stockApiService.getTickerQuote(tickerSymbol);
        Assert.assertEquals(expectedTickerRealTimeInfo, actualTickerRealTimeInfo);
    }

    @Test(expected = ResponseErrorException.class)
    public void tickerQuoteResponse_responseErrorExceptionTestWithBadRequest() {
        TickerRealTimeInfo tickerRealTimeInfo = new TickerRealTimeInfo();
        when(restTemplate.getForEntity(TickerQuoteURI, TickerRealTimeInfo.class))
                .thenReturn(new ResponseEntity<>(tickerRealTimeInfo, HttpStatus.BAD_REQUEST));
        stockApiService.getTickerQuote(tickerSymbol);
    }


}
