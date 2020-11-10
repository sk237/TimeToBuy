package com.example.demo.model.stock.api.finnhub.candles;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TickerHistoryResponse {
    @JsonProperty("o")
    List<Float> open;

    @JsonProperty("h")
    List<Float> high;

    @JsonProperty("l")
    List<Float> low;

    @JsonProperty("c")
    List<Float> close;

    @JsonProperty("v")
    List<Float> volume;

    @JsonProperty("t")
    List<Long> timestamp;

    @JsonProperty("s")
    String status;
}


//        o
//        List of open prices for returned candles.
//
//        h
//        List of high prices for returned candles.
//
//        l
//        List of low prices for returned candles.
//
//        c
//        List of close prices for returned candles.
//
//        v
//        List of volume data for returned candles.
//
//        t
//        List of timestamp for returned candles.
//
//        s
//        Status of the response. This field can either be ok or no_data.
//
