package com.jdum.commerce.sumysoul.web.rest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdum.commerce.sumysoul.domain.Authority;
import com.jdum.commerce.sumysoul.dto.UserDto;
import com.jdum.commerce.sumysoul.service.UserService;
import java.util.EnumSet;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(
    controllers = UserController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class UserControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService userService;

  @Autowired
  private ObjectMapper mapper;

  @SneakyThrows
  @Test
  void shouldGetAllUsers() {
    var user = buildUser();
    when(userService.getAll()).thenReturn(List.of(user));
    mvc.perform(MockMvcRequestBuilders.get("/api/v1/user")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(mapper.writeValueAsString(List.of(user))));
  }

  @SneakyThrows
  @Test
  void shouldGetUserById() {
    var user = buildUser();
    when(userService.get("123")).thenReturn(user);
    mvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}", "123"))
        .andExpect(status().isOk())
        .andExpect(content().string(mapper.writeValueAsString(user)));
  }

  @SneakyThrows
  @Test
  void shouldCreateUser() {
    var user = buildUser();
    when(userService.create(user)).thenReturn(user);
    mvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
            .content(mapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(mapper.writeValueAsString(user)));
  }

  @SneakyThrows
  @Test
  void shouldUpdateUser() {
    var user = buildUser();
    var userUpdated = buildUser();
    userUpdated.setId("123");
    when(userService.update("123", user)).thenReturn(userUpdated);
    mvc.perform(MockMvcRequestBuilders.put("/api/v1/user/{id}", "123")
            .content(mapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(mapper.writeValueAsString(userUpdated)));
  }

  @SneakyThrows
  @Test
  void shouldDeleteUser() {
    doNothing().when(userService).delete("123");
    mvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/{id}", "123"))
        .andExpect(status().isOk())
        .andExpect(content().string(""));
  }

  private UserDto buildUser() {
    return UserDto.builder()
        .email("test@email.com")
        .firstName("testName")
        .lastName("testLastName")
        .login("testLogin")
        .password("testPassword")
        .authorities(EnumSet.of(Authority.SUPER_ADMIN))
        .build();
  }
}
