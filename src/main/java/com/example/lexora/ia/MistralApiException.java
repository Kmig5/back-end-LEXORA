package com.example.lexora.ia;

public class MistralApiException extends RuntimeException {

    private final int statusCode;

    public MistralApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public MistralApiException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
