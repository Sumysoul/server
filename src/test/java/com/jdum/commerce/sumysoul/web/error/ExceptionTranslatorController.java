package com.jdum.commerce.sumysoul.web.error;

import com.jdum.commerce.sumysoul.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionTranslatorController {

  @GetMapping("/test/user-not-found")
  public void userNotFoundError() {
    throw new UnauthorizedException("test user not found error");
  }

  @GetMapping("/test/not-found")
  public void notFoundError() {
    throw new NotFoundException("test not found error");
  }

  @GetMapping("/test/unauthorized")
  public void unauthorizedError() {
    throw new BadCredentialsException("test authentication failed");
  }

  @GetMapping("/test/user-exists")
  public void userExistsError() {
    throw new UserExistsException("test userExists error");
  }

  @GetMapping("/test/access-denied")
  public void accessDeniedError() {
    throw new AccessDeniedException("test accessDenied error");
  }

  @PostMapping("/test/validation")
  public void methodArgumentNotValidError(@Valid @RequestBody LoginRequest loginRequest) {
  }

  @GetMapping("/test/internal-server-error")
  public void internalServerError() {
    throw new RuntimeException();
  }
}
