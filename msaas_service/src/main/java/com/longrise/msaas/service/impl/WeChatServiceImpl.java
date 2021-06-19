package com.longrise.msaas.service.impl;

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

  private RestTemplate restTemplate;

  private static EntityBean storage = new EntityBean();

  @Autowired
  public WeChatServiceImpl( @NonNull RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public EntityBean getAccessToken(String url) {
    if(storage.isEmpty()){
      init(url);
    }else{
      long cur_timestamp = SignatureTool.create_timestamp(),
      val = cur_timestamp - storage.getLong("timestamp");
      if(val>=7200){
        init(url);
      }
    }
    return  storage;
  }

  private void init(String url) {
    getToken();
    getTicket();
    if(!storage.isEmpty()){
      EntityBean sign_bean = SignatureTool.sign(storage.getString("ticket"), url);
      storage.put("expires_in", 7200);
      storage.putAll(sign_bean);
    }
  }

  /**
   * 获取访问token
   */
  private void getToken(){
    EntityBean access_token_bean = restTemplate.getForObject(String.format(token_url, appid, appsecret), EntityBean.class);
    if(Objects.nonNull(access_token_bean) && access_token_bean.containsKey("access_token")){
      storage.put("access_token", access_token_bean.getString("access_token"));
      storage.put("appId", appid);
    }else{
      storage = new EntityBean();
    }
  }

  /**
   * 根据access_token获取票根
   */
  private void getTicket(){
    if(storage.containsKey("access_token")){
      String access_token = storage.getString("access_token");
      EntityBean ticket_bean = restTemplate.getForObject(String.format(ticket_url, access_token), EntityBean.class);
      if(Objects.nonNull(ticket_bean) && ticket_bean.containsKey("ticket")){
        storage.put("ticket", ticket_bean.getString("ticket"));
        return;
      }
    }
    storage = new EntityBean();
  }
}
