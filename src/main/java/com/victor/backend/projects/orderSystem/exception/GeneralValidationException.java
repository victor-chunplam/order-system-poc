package com.victor.backend.projects.orderSystem.exception;

public class GeneralValidationException extends RuntimeException {
    public GeneralValidationException(String message) {
        super(message);
    }

    public GeneralValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
