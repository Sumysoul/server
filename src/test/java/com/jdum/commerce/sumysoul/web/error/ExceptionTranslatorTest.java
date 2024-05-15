package com.jdum.commerce.sumysoul.web.error;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdum.commerce.sumysoul.dto.LoginRequest;
import com.jdum.commerce.sumysoul.service.UserService;
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
    controllers = ExceptionTranslatorController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class ExceptionTranslatorTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService userService;

  @Autowired
  private ObjectMapper mapper;

  @SneakyThrows
  @Test
  void shouldThrowUserNotFoundError() {
    mvc.perform(MockMvcRequestBuilders.get("/test/user-not-found")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("test user not found error"))
        .andExpect(jsonPath("$.status").value(401));
  }

  @SneakyThrows
  @Test
  void shouldThrowNotFoundError() {
    mvc.perform(MockMvcRequestBuilders.get("/test/not-found")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("test not found error"))
        .andExpect(jsonPath("$.status").value(404));
  }

  @SneakyThrows
  @Test
  void shouldThrowUnauthorizedError() {
    mvc.perform(MockMvcRequestBuilders.get("/test/unauthorized")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("test authentication failed"))
        .andExpect(jsonPath("$.status").value(401));
  }

  @SneakyThrows
  @Test
  void shouldThrowUserExistsError() {
    mvc.perform(MockMvcRequestBuilders.get("/test/user-exists")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("test userExists error"))
        .andExpect(jsonPath("$.status").value(409));
  }

  @SneakyThrows
  @Test
  void shouldThrowAccessDeniedError() {
    mvc.perform(MockMvcRequestBuilders.get("/test/access-denied")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("test accessDenied error"))
        .andExpect(jsonPath("$.status").value(403));
  }

  @SneakyThrows
  @Test
  void shouldThrowMethodArgumentNotValidError() {
    mvc.perform(MockMvcRequestBuilders.post("/test/validation")
            .content(mapper.writeValueAsString(new LoginRequest("name", null)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title").value("Method argument not valid"))
        .andExpect(jsonPath("$.details[0].field").value("password"))
        .andExpect(jsonPath("$.details[0].error").value("Should not be empty"))
        .andExpect(jsonPath("$.status").value(400));
  }
}
