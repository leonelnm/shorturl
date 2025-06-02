package com.codigo04.shorturl.exception;

public class ShortUrlArgumentException extends RuntimeException{

    private final String message;

    public ShortUrlArgumentException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
