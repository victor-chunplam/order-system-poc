package com.victor.backend.projects.orderSystem.exception;

public class OrderHasTakenException extends RuntimeException {
    public OrderHasTakenException(String message) {
        super(message);
    }

    public OrderHasTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
