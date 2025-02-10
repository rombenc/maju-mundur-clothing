package com.majumundur.clothing.exception;

public class OrderException extends RuntimeException {
    public OrderException(String message, int i) {
        super(message);
    }
}
