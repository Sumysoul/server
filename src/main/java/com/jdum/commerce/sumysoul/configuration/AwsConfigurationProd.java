package com.jdum.commerce.sumysoul.configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!local")
public class AwsConfigurationProd {

  @Bean
  public AmazonS3 s3() {
    return AmazonS3ClientBuilder.standard().build();
  }
}
