package com.majumundur.clothing.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message, int i) {
        super(message);
    }
}
