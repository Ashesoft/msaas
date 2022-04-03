package com.longrise.msaas.web.configuration;

import com.longrise.msaas.global.domain.WeChatCache;
import com.longrise.msaas.global.handler.WeChatCacheHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatTokenCacheConfig {
  @Autowired
  private WeChatCacheHandler weChatCacheHandler;

  @Bean
  public WeChatCache getWeChatTokenCache() {
    return new WeChatCache(weChatCacheHandler);
  }
}
