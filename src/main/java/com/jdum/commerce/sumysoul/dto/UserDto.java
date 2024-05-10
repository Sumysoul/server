package com.jdum.commerce.sumysoul.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jdum.commerce.sumysoul.configuration.Authority;
import java.util.EnumSet;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {
  private String id;
  private String login;
  private String email;
  private String password;
  private EnumSet<Authority> authorities;
}
