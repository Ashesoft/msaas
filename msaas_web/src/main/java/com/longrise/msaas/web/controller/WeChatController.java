package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeChatController {

  @Autowired
  private WeChatService weChatService;

  @Autowired
  private WeChatController(RestTemplate restTemplate, WeChatService weChatService){
    this.weChatService = weChatService;
  }

  @GetMapping("/signature")
  public EntityBean signature(@RequestParam String url) {
    return weChatService.getAccessToken(url);
  }
}
