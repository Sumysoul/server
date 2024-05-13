package com.jdum.commerce.sumysoul.configuration;

import com.jdum.commerce.sumysoul.configuration.security.jwt.JwtAuthenticationEntryPoint;
import com.jdum.commerce.sumysoul.configuration.security.jwt.JwtAuthenticationFilter;
import com.jdum.commerce.sumysoul.configuration.security.jwt.JwtHelper;
import com.jdum.commerce.sumysoul.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtHelper jwtHelper;
  private final UserService userDetailsService;
  private final JwtAuthenticationEntryPoint point;

  @Bean
  public JwtAuthenticationFilter authFilter() {
    return new JwtAuthenticationFilter(jwtHelper, userDetailsService);
  }

  @Bean
  public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**")
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers("/api/login").permitAll();
          auth.requestMatchers("/api/v1/drinks").permitAll();
          auth.requestMatchers("/api/v1/food").permitAll();
          auth.anyRequest().authenticated();
        })
        .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
        .authenticationProvider(authenticationProvider());
    return http.build();
  }


  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
