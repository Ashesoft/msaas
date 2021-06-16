package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.utils.SignatureTool;
import com.longrise.msaas.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
public class WeChatController {
  private static EntityBean token;

  @Autowired
  private WeChatService weChatService;

  @Autowired
  private WeChatController(RestTemplate restTemplate, WeChatService weChatService){
    this.weChatService = weChatService;
  }

  @GetMapping("/signature")
  public EntityBean signature() {
    if (Objects.nonNull(token) && token.containsKey("expires_in")) {
      int expires_in = token.getInt("expires_in");
      long timestamp = token.getLong("timestamp");
      long val = SignatureTool.getCurrentTimeSeconds() - timestamp;
      if(val >= expires_in ){
       token = weChatService.getAccessToken();
      }
    }else{
      token = weChatService.getAccessToken();
    }
    System.out.println(token);
    return token;
  }
}
