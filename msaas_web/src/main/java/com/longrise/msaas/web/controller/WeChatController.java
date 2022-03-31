package com.longrise.msaas.web.controller;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wechat")
public class WeChatController {

  @Autowired
  private final WeChatService weChatService;

  @Autowired
  private WeChatController(WeChatService weChatService) {
    this.weChatService = weChatService;
  }


  @GetMapping("/signature")
  public EntityBean signature(@RequestParam String wxid, @RequestParam String url) {
    return weChatService.signature(wxid, url);
  }

  @GetMapping("/getWxUserInfo")
  public EntityBean getWxUserInfo(@RequestParam String code, @RequestParam String wxid, @RequestParam(required = false) String openid) {
    return weChatService.getWxUserInfo(code, wxid, openid);
  }

  @PostMapping("/addwxcfg")
  public boolean addWxConfig(@RequestParam String appid, @RequestParam String appsecret) {
    return weChatService.addWxConfig(appid, appsecret);
  }

  @GetMapping("/autoauth")
  public String autoAuth(@RequestParam String wxid) {
    return weChatService.autoAuth(wxid);
  }
}
