package com.jdum.commerce.sumysoul.web.error;

public class UserExistsException extends RuntimeException {
  public UserExistsException(String message) {
    super(message);
  }
}
