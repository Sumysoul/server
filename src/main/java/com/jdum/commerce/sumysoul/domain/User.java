package com.jdum.commerce.sumysoul.domain;

import com.jdum.commerce.sumysoul.configuration.Authority;
import java.util.EnumSet;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
@Data
@Builder
public class User {
  private String id;
  private String login;
  private String email;
  private String firstName;
  private String lastName;
  private String password;
  private EnumSet<Authority> authorities;
}
