package com.jdum.commerce.sumysoul.web.error;

public class UserExistsException extends RuntimeException {
  private String message;

  public UserExistsException(String message) {
    super(message);
  }
}
