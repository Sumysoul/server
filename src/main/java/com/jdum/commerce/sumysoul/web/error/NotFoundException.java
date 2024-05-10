package com.jdum.commerce.sumysoul.web.error;

public class NotFoundException extends RuntimeException {
  private String message;

  public NotFoundException(String message) {
    super(message);
  }
}
