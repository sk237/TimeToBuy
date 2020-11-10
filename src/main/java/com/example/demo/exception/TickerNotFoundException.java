package com.example.demo.exception;

public class TickerNotFoundException extends RuntimeException {
    public TickerNotFoundException() {
        super("Invalid Ticker Symbol");
    }
}
