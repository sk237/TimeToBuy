package com.example.demo.model.stock.api.finnhub.quote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickerRealTimeInfo {
    @JsonProperty("o")
    float open;
    @JsonProperty("h")
    float high;
    @JsonProperty("l")
    float low;
    @JsonProperty("c")
    float current;
    @JsonProperty("pc")
    float prevClose;
}


//        o
//        Open price of the day
//
//        h
//        High price of the day
//
//        l
//        Low price of the day
//
//        c
//        Current price
//
//        pc
//        Previous close price