package com.example.exception;

public class LoggingStartupException extends RuntimeException {
    public LoggingStartupException() {}

    public LoggingStartupException(String message) {
        super(message);
    }
}

