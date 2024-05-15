package com.jdum.commerce.sumysoul.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdum.commerce.sumysoul.configuration.security.jwt.JwtHelper;
import com.jdum.commerce.sumysoul.domain.Authority;
import com.jdum.commerce.sumysoul.dto.LoginRequest;
import com.jdum.commerce.sumysoul.service.UserService;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(
    controllers = AuthController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class AuthControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private UserService userService;

  @MockBean
  private AuthenticationManagerBuilder authenticationManagerBuilder;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private JwtHelper jwtHelper;

  @BeforeEach
  void setUp() {
    when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
  }

  @SneakyThrows
  @Test
  void shouldReturnJwtIfUserCredentialsValid() {

    var loginRequest = new LoginRequest("userName", "password");
    var userDetails =
        new org.springframework.security.core.userdetails.User(loginRequest.login(),
            loginRequest.password(),
            List.of(new SimpleGrantedAuthority(Authority.SUPER_ADMIN.name())));

    var auth = new UsernamePasswordAuthenticationToken(userDetails, null);
    when(authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.login(), loginRequest.password()))).thenReturn(
        auth);
    when(jwtHelper.generateJwtToken(auth)).thenReturn("123");

    mvc.perform(MockMvcRequestBuilders.post("/api/login")
            .content(mapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id_token").value("123"));
  }

  @SneakyThrows
  @Test
  void shouldNotReturnJwtIfUserCredentialsInvalid() {

    var loginRequest = new LoginRequest("userName", "password");

    when(authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.login(), loginRequest.password()))).thenThrow(
        new BadCredentialsException("Bad credentials"));

    mvc.perform(MockMvcRequestBuilders.post("/api/login")
            .content(mapper.writeValueAsString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.title").value("Bad credentials"))
        .andExpect(jsonPath("$.status").value(401));
  }
}
