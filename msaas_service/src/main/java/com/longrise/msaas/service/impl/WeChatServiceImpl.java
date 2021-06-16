package com.longrise.msaas.service.impl;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.utils.SignatureTool;
import com.longrise.msaas.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeChatServiceImpl implements WeChatService {
  @Value("${wx.config.appid}")
  private String appid;
  @Value("${wx.config.appsecret}")
  private String appsecret;
  @Value("${wx.config.turl}")
  private String url;
  @Value("${wx.config.noncestr}")
  private String noncestr;

  private RestTemplate restTemplate;

  @Autowired
  public WeChatServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public EntityBean getAccessToken() {
    EntityBean bean = restTemplate.getForObject(String.format(url, appid, appsecret), EntityBean.class);
    bean.put("appid", appid);
    bean.put("timestamp", SignatureTool.getCurrentTimeSeconds());
    bean.put("noncestr", noncestr);
    return bean;
  }

}
