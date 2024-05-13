package com.jdum.commerce.sumysoul.configuration;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfiguration {
  @Bean
  public CorsFilter corsFilter() {
    var source = new UrlBasedCorsConfigurationSource();
    var config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowCredentials(true);
    config.setAllowedHeaders(List.of("*"));
    config.setAllowedMethods(List.of("*"));
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
