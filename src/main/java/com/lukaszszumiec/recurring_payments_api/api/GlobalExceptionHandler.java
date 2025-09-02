package com.lukaszszumiec.recurring_payments_api.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Resource not found");
        pd.setDetail(ex.getMessage());
        return pd; // RFC7807
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleGeneric(Exception ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Unexpected error");
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
