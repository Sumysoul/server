package com.jdum.commerce.sumysoul.web.rest;

import com.jdum.commerce.sumysoul.configuration.security.jwt.JwtHelper;
import com.jdum.commerce.sumysoul.dto.JwtResponse;
import com.jdum.commerce.sumysoul.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtHelper jwtHelper;

  @PostMapping("/login")
  public JwtResponse login(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtHelper.generateJwtToken(authentication);

    return new JwtResponse(jwt);
  }
}
