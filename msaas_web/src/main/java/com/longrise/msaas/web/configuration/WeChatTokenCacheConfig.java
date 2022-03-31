package com.longrise.msaas.web.configuration;

import com.longrise.msaas.global.domain.WeChatTokenCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatTokenCacheConfig {

  @Bean
  public WeChatTokenCache getWeChatTokenCache() {
    return new WeChatTokenCache();
  }
}
