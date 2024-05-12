package com.jdum.commerce.sumysoul.web.error;

import com.jdum.commerce.sumysoul.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class ExceptionTranslator {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleMenuNotFoundError(NotFoundException e) {
    return ErrorResponse.builder()
        .title(e.getLocalizedMessage())
        .status(HttpStatus.NOT_FOUND.value())
        .build();
  }

  @ExceptionHandler(UnauthorizedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleUserNotFoundError(UnauthorizedException e) {
    return ErrorResponse.builder()
        .title(e.getLocalizedMessage())
        .status(HttpStatus.UNAUTHORIZED.value())
        .build();
  }

  @ExceptionHandler(UserExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleUserNotFoundError(UserExistsException e) {
    return ErrorResponse.builder()
        .title(e.getLocalizedMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorResponse handleAccessDeniedError(AccessDeniedException e) {
    return ErrorResponse.builder()
        .title(e.getLocalizedMessage())
        .status(HttpStatus.FORBIDDEN.value())
        .build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationError(MethodArgumentNotValidException e) {
    return ErrorResponse.builder()
        .title(e.getLocalizedMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleValidationError(AuthenticationException e) {
    return ErrorResponse.builder()
        .title(e.getLocalizedMessage())
        .status(HttpStatus.UNAUTHORIZED.value())
        .build();
  }
}
