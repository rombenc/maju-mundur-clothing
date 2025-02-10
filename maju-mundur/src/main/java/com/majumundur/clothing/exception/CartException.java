package com.majumundur.clothing.exception;

public class CartException extends RuntimeException {
  public CartException(String message, int statusCode) {
    super(message);
  }
}
