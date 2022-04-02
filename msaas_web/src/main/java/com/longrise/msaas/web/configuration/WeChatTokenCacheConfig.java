package com.longrise.msaas.web.configuration;

import com.longrise.msaas.global.domain.WeChatTokenCache;
import com.longrise.msaas.global.handler.WeChatCacheHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatTokenCacheConfig {
  @Autowired
  private WeChatCacheHandler weChatCacheHandler;

  @Bean
  public WeChatTokenCache getWeChatTokenCache() {
    return new WeChatTokenCache(weChatCacheHandler);
  }
}
