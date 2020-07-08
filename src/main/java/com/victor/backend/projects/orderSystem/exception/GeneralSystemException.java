package com.victor.backend.projects.orderSystem.exception;

public class GeneralSystemException extends RuntimeException {
    public GeneralSystemException(String message) {
        super(message);
    }

    public GeneralSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
