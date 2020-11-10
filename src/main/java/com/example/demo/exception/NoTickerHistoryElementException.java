package com.example.demo.exception;

public class NoTickerHistoryElementException extends RuntimeException {
    public NoTickerHistoryElementException() {
        super("No MaxProfitData Element in Database");
    }
}
