package com.jdum.commerce.sumysoul;

import com.jdum.commerce.sumysoul.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
@EnableWebMvc
@RequiredArgsConstructor
@Slf4j
public class ServerlessApplication extends SpringBootServletInitializer {

  private final UserService userService;

  public static void main(String[] args) {
    SpringApplication.run(ServerlessApplication.class, args);
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    super.onStartup(servletContext);
    userService.initDB();
  }

  /**
   * Create required HandlerMapping, to avoid several default HandlerMapping instances being created
   */
  @Bean
  public HandlerMapping handlerMapping() {
    return new RequestMappingHandlerMapping();
  }

  /**
   * Create required HandlerAdapter, to avoid several default HandlerAdapter instances being created
   */
  @Bean
  public HandlerAdapter handlerAdapter() {
    return new RequestMappingHandlerAdapter();
  }

  @Bean
  public ApplicationRunner applicationRunner() {
    return args -> userService.initDB();
  }
}
