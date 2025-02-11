package com.majumundur.clothing.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message, int i) {
        super(message);
    }
}
