package com.majumundur.clothing.exception;

public class UnverifiedUserException extends RuntimeException {
    public UnverifiedUserException(String message, int i) {
        super(message);
    }
}
