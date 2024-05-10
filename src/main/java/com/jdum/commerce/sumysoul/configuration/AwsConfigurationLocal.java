package com.jdum.commerce.sumysoul.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class AwsConfigurationLocal {

  @Value("${cloud.aws.region.static}")
  private String region;

  @Value("${cloud.aws.credentials.profile-name}")
  private String profileName;

  @Bean
  public AmazonS3 s3() {
    return AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(
            new ProfileCredentialsProvider(profileName).getCredentials()))
        .withRegion(Regions.fromName(region))
        .build();
  }
}
