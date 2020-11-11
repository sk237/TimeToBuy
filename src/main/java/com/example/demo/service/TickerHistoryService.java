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
import java.util.ArrayList;
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
    // Then, this method calculate average, upper bound, and lower bound prices based on today's query.
    // The response must contain stock's high prices and low prices for the last 120 days.
    // For the stocks that don't have 120 days of data, at least a day of historical data should be given.
    protected TickerHistory findAverageTickerPrice(String tickerSymbol, TickerHistoryResponse TickerHistoryResponse) {
        if (TickerHistoryResponse.getStatus().equals("no_data"))
            throw new TickerNotFoundException();
        if (TickerHistoryResponse.getHigh().size() < 1)
            throw new NotEnoughTickerHistoryException();

        List<Float> highList = TickerHistoryResponse.getHigh();
        List<Float> lowList = TickerHistoryResponse.getLow();
        List<Float> upperBoundList = new ArrayList<>();
        List<Float> lowerBoundList = new ArrayList<>();
        float totalSum = 0, upperBound = 0, lowerBound = 0, avg = 0;
        int candleSize = lowList.size();

        for (int i = 0; i < candleSize; i++) {
            float high = highList.get(i);
            float low = lowList.get(i);
            totalSum += (high - low) / 2 + low;
            avg = totalSum / (i + 1);
            if (high > avg)
                upperBoundList.add(high);
            if (low < avg)
                lowerBoundList.add(low);
        }

        for (float price : upperBoundList)
            upperBound += price;
        upperBound /= upperBoundList.size();

        for (float price : lowerBoundList)
            lowerBound += price;
        lowerBound /= lowerBoundList.size();

        return new TickerHistory(tickerSymbol, avg, upperBound, lowerBound);
    }

    public TickerHistory getTickerDataInfo(String tickerSymbol) {
        Timestamp today = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Optional<TickerHistory> optionalTicker = tickerHistoryRepository.findBySymbolAndDate(tickerSymbol, today);
        return optionalTicker.orElseThrow(NotEnoughTickerHistoryException::new);
    }
}
