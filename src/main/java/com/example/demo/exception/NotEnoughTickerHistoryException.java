package com.example.demo.exception;


public class NotEnoughTickerHistoryException extends RuntimeException {
    public NotEnoughTickerHistoryException() {
        super("Ticker History is not enough to find Max Profit");
    }
}
