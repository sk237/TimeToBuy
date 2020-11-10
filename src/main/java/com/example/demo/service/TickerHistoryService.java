package com.example.demo.service;

import com.example.demo.exception.NotEnoughTickerHistoryException;
import com.example.demo.exception.TickerNotFoundException;
import com.example.demo.model.TickerHistory;
import com.example.demo.model.stock.api.finnhub.quote.TickerRealTimeInfo;
import com.example.demo.model.stock.api.finnhub.candles.TickerHistoryResponse;
import com.example.demo.repository.TickerHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TickerHistoryService {

    private final TickerHistoryRepository tickerHistoryRepository;
    private final StockApiService stockApiService;

    public void postTickerDataInfo(String tickerSymbol) {
        TickerHistory tickerHistory = getTickerHistory(tickerSymbol);
        TickerRealTimeInfo tickerRealTimeInfo = stockApiService.getTickerQuote(tickerSymbol);
        tickerHistory.setCurrentPrice(tickerRealTimeInfo.getCurrent());
        tickerHistoryRepository.save(tickerHistory);
    }

    protected TickerHistory getTickerHistory(String tickerSymbol) {
        Timestamp today = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Optional<TickerHistory> optionalTicker = tickerHistoryRepository.findBySymbolAndDate(tickerSymbol, today);
        return optionalTicker.orElse(makeTickerHistory(tickerSymbol));
    }

    protected TickerHistory makeTickerHistory(String tickerSymbol) {
        TickerHistoryResponse tickerHistoryResponse= stockApiService.getTickerHistory(tickerSymbol);
        return findAverageTickerPrice(tickerSymbol, tickerHistoryResponse);
    }

    // This method take in Ticker's symbol String and response from third-party stock data api.
    // Then, this method calculate average price based on today's query.
    // The response must contain stock's high prices and low prices for the last 60 days.
    // For the stocks that don't have 60 days of data, at least a day of historical data should be given.
    protected TickerHistory findAverageTickerPrice(String tickerSymbol, TickerHistoryResponse TickerHistoryResponse) {
        if (TickerHistoryResponse.getStatus().equals("no_data"))
            throw new TickerNotFoundException();
        if (TickerHistoryResponse.getHigh().size() < 1)
            throw new NotEnoughTickerHistoryException();

        List<Float> lowList = TickerHistoryResponse.getLow();
        List<Float> highList = TickerHistoryResponse.getHigh();
        int candleSize = lowList.size();
        float avg = 0;
        for (int i = 0; i < candleSize; i++) {
            float high = highList.get(i);
            float low = lowList.get(i);
            avg += (high - low) / 2 + low;
        }
        avg /= candleSize;
        return new TickerHistory(tickerSymbol, avg);
    }

    public TickerHistory getTickerDataInfo(String tickerSymbol) {
        Timestamp today = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Optional<TickerHistory> optionalTicker = tickerHistoryRepository.findBySymbolAndDate(tickerSymbol, today);
        return optionalTicker.orElseThrow(NotEnoughTickerHistoryException::new);
    }
}
