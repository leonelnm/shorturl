package com.codigo04.shorturl.controller;

import com.codigo04.shorturl.exception.ShortUrlArgumentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShortUrlArgumentException.class)
    public ResponseEntity<ErrorResponse> handleShortUrlArgumentException(ShortUrlArgumentException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    // DTO para la respuesta de error
    public record ErrorResponse(String message) {}

}
