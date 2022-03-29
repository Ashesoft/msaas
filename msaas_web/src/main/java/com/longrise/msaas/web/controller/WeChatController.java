package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.domain.WeChatTokenCache;
import com.longrise.msaas.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
public class WeChatController {

  @Autowired
  private final WeChatService weChatService;

  @Autowired
  private WeChatController(WeChatService weChatService) {
    this.weChatService = weChatService;
  }

  @GetMapping("/signature")
  public EntityBean signature(@RequestParam String url) {
    return weChatService.getAccessToken(url);
  }

  @GetMapping("/getWxUserInfo")
  public EntityBean getWxUserInfo(@RequestParam String code) {
    return weChatService.getWxUserInfo(code);
  }

  @GetMapping("/getAppId")
  public String getAppId() {
    return weChatService.getAppid();
  }
}
