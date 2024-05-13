package com.jdum.commerce.sumysoul.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jdum.commerce.sumysoul.domain.Authority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.EnumSet;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {
  private String id;

  @NotEmpty(message = "Should not be empty")
  private String login;

  @NotEmpty(message = "Should not be empty")
  @Email(message = "Should have email format")
  private String email;

  @NotEmpty(message = "Should not be empty")
  private String password;

  private String firstName;
  private String lastName;

  private EnumSet<Authority> authorities;
}
