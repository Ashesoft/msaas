package com.longrise.msaas.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTempConfig {
  private RestTemplateBuilder restTemplateBuilder;

  @Autowired
  public RestTempConfig(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplateBuilder = restTemplateBuilder;
  }

  @Bean
  public RestTemplate restTemplate() {
    return restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(20)).setReadTimeout(Duration.ofSeconds(20)).build();
  }
}
