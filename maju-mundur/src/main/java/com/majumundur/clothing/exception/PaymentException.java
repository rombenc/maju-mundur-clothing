package com.majumundur.clothing.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String message, int statusCode) {
        super(message);
    }
}
