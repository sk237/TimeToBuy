package com.example.demo.service;

import com.example.demo.exception.ResponseErrorException;
import com.example.demo.model.stock.api.finnhub.quote.TickerRealTimeInfo;
import com.example.demo.model.stock.api.finnhub.candles.TickerHistoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:values.properties")
public class StockApiService {

    private final RestTemplate restTemplate;

    @Value("${apiKey}")
    private String apiKey;

    public TickerHistoryResponse getTickerHistory(String tickerSymbol) {
        long currentTimeMillis = System.currentTimeMillis(), sixtyDayByMillis = 5184000L;
        long end = currentTimeMillis / 1000;
        long start = end - sixtyDayByMillis;
        URI uri = URI.create("https://finnhub.io/api/v1/stock/candle?symbol=" + tickerSymbol
                + "&resolution=D&from=" + start + "&to=" + end + "&token=" + apiKey);
        ResponseEntity<TickerHistoryResponse> res = restTemplate.getForEntity(uri, TickerHistoryResponse.class);
        if (res.getStatusCode() == HttpStatus.OK)
            return res.getBody();
        else
            throw new ResponseErrorException(res.getStatusCode() + " : " + res.getBody());

    }

    public TickerRealTimeInfo getTickerQuote(String tickerSymbol) {
        URI uri = URI.create("https://finnhub.io/api/v1/quote?symbol=" + tickerSymbol + "&token=" + apiKey);
        ResponseEntity<TickerRealTimeInfo> res = restTemplate.getForEntity(uri, TickerRealTimeInfo.class);
        if (res.getStatusCode() == HttpStatus.OK)
            return res.getBody();
        else
            throw new ResponseErrorException(res.getStatusCode() + " : " + res.getBody());
    }
}
