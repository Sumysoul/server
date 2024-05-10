package com.jdum.commerce.sumysoul.web.error;

public class UnauthorizedException extends RuntimeException {
  private String message;

  public UnauthorizedException(String message) {
    super(message);
  }
}
