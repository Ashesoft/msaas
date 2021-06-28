package com.longrise.msaas.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.utils.SignatureTool;
import com.longrise.msaas.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class WeChatServiceImpl implements WeChatService {
  @Value("${wx.config.appid}")
  private String appid;
  @Value("${wx.config.appsecret}")
  private String appsecret;
  @Value("${wx.config.token_url}")
  private String token_url;
  @Value("${wx.config.ticket_url}")
  private String ticket_url;
  @Value("${wx.config.user_token_url}")
  private String user_token_url;
  @Value("${wx.config.user_url}")
  private String user_url;


  private final RestTemplate restTemplate;

  private static EntityBean storage = new EntityBean();
  private static final EntityBean userstorage = new EntityBean();

  @Autowired
  public WeChatServiceImpl(@NonNull RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public EntityBean getAccessToken(String url) {
    if (storage.isEmpty()) {
      init(url);
    } else if (isExpirseIn(storage)) {
      init(url);
    }
    return storage;
  }

  @Override
  public EntityBean getWxUserInfo(String code) {
    if (userstorage.isEmpty()) {
      getUserToken(code);
    } else if (isExpirseIn(userstorage)) {
      getUserToken(code);
    }
    if (userstorage.containsKey("access_token") && userstorage.containsKey("openid")) {
      String user = restTemplate.getForObject(String.format(user_url, userstorage.getString("access_token"), userstorage.getString("openid")), String.class);
      try {
        return new ObjectMapper().readValue(user, EntityBean.class);
      } catch (JsonProcessingException e) {
        throw new APIException(5001, "user 反序列化失败");
      }
    }
    return null;
  }

  private void getUserToken(String code) {
    String user_token = restTemplate.getForObject(String.format(user_token_url, appid, appsecret, code), String.class);
    try {
      EntityBean user_bean_token = new ObjectMapper().readValue(user_token, EntityBean.class);
      userstorage.putAll(user_bean_token);
    } catch (JsonProcessingException e) {
      throw new APIException(5001, "user_bean_token 反序列化失败");
    }
  }

  @Override
  public String getAppid() {
    return appid;
  }

  private boolean isExpirseIn(EntityBean bean) {
    long cur_timestamp = SignatureTool.create_timestamp(),
        val = cur_timestamp - bean.getLong("timestamp");
    return val < 7200;
  }

  private void init(String url) {
    getToken();
    getTicket();
    if (!storage.isEmpty()) {
      EntityBean sign_bean = SignatureTool.sign(storage.getString("ticket"), url);
      storage.put("expires_in", 7200);
      storage.putAll(sign_bean);
    }
  }

  /**
   * 获取访问token
   */
  private void getToken() {
    EntityBean access_token_bean = restTemplate.getForObject(String.format(token_url, appid, appsecret), EntityBean.class);
    if (Objects.nonNull(access_token_bean) && access_token_bean.containsKey("access_token")) {
      storage.put("access_token", access_token_bean.getString("access_token"));
      storage.put("appId", appid);
    } else {
      storage = new EntityBean();
    }
  }

  /**
   * 根据access_token获取票根
   */
  private void getTicket() {
    if (storage.containsKey("access_token")) {
      String access_token = storage.getString("access_token");
      EntityBean ticket_bean = restTemplate.getForObject(String.format(ticket_url, access_token), EntityBean.class);
      if (Objects.nonNull(ticket_bean) && ticket_bean.containsKey("ticket")) {
        storage.put("ticket", ticket_bean.getString("ticket"));
        return;
      }
    }
    storage = new EntityBean();
  }
}
