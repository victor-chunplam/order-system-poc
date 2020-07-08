package com.victor.backend.projects.orderSystem.controller;

import com.victor.backend.projects.orderSystem.exception.GeneralSystemException;
import com.victor.backend.projects.orderSystem.exception.GeneralValidationException;
import com.victor.backend.projects.orderSystem.exception.OrderHasTakenException;
import com.victor.backend.projects.orderSystem.pojo.resp.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice(basePackages = "com.victor.backend.projects.orderSystem.controller")
@Slf4j
public class DefaultExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations()
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Invalid/Empty input value");

        log.info("handleConstraintViolationException: {}", e.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .error(message)
                        .build());
    }

    @ExceptionHandler({GeneralValidationException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleGeneralValidationException(GeneralValidationException e) {
        log.error("handleGeneralValidationException: ", e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler({GeneralSystemException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleGeneralSystemException(Exception e) {
        log.error("handleGeneralSystemException: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler({OrderHasTakenException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleOrderHasTakenException(OrderHasTakenException e) {
        log.error("handleOrderHasTakenException: ", e);

        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(ErrorResponse.builder()
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .error(e.getMessage())
                        .build());
    }
}
