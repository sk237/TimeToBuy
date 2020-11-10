package com.example.demo.exception;

public class ResponseErrorException extends RuntimeException{
    public ResponseErrorException(String err) {
        super("Response Error : " + err);
    }
}
