package com.jdum.commerce.sumysoul.web.error;

public class ApplicationException extends RuntimeException {
  public ApplicationException(String message, Throwable e) {
    super(message, e);
  }
}
