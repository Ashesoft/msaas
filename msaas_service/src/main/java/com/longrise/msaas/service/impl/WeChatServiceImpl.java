package com.longrise.msaas.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.domain.WeChatTokenCache;
import com.longrise.msaas.global.utils.SignatureTool;
import com.longrise.msaas.mapping.WeChatMapping;
import com.longrise.msaas.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class WeChatServiceImpl implements WeChatService {

  // 通过它获取公众号的全局唯一接口调用凭据, 公众号调用个接口时都需要使用他 `access_token`, 有效期为2个小时, 需定时刷新;
  @Value("${wx.config.token_url}")
  private String token_url;

  // jsapi 签证的票根
  @Value("${wx.config.ticket_url}")
  private String ticket_url;

  @Value("${wx.config.redirect_url}")
  private String redirect_url;

  // 用户授权可以获取信息的access_token
  @Value("${wx.config.user_token_url}")
  private String user_token_url;

  // 刷新用户授权获取信息的access_token
  @Value("${wx.config.user_refresh_token_url}")
  private String user_refresh_token_url;

  // 网页授权获取用户信息
  @Value("${wx.config.user_url}")
  private String user_url;


  private RestTemplate restTemplate;
  private WeChatTokenCache weChatTokenCache;
  private WeChatMapping weChatMapping;

  private EntityBean storage = new EntityBean();

  @Autowired
  public WeChatServiceImpl(@NonNull RestTemplate restTemplate, WeChatTokenCache weChatTokenCache, WeChatMapping weChatMapping) {
    this.restTemplate = restTemplate;
    this.weChatTokenCache = weChatTokenCache;
    this.weChatMapping = weChatMapping;
  }

  @Override
  public EntityBean signature(String wxid, String url) {
    String ticket = getTicket(wxid);
    EntityBean sign = SignatureTool.sign(ticket, url);
    sign.put("appid", getAppidById(wxid));
    return sign;
  }


  /**
   * 1. 通过用户手动授权或静默授权获取code
   * <p>
   * 2. 通过code换取网页授权的 access_token
   * <p>
   * 3. 刷新access_token(如果需要)
   * <p>
   * 4. 拉取用户信息
   * <p>
   * <a href='https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html'>详细说明</a>
   */
  @Override
  public EntityBean getWxUserInfo(String code, String wxid, String openid) {
    EntityBean user_token, user_info;
    if (StringUtils.isEmpty(openid)) {
      user_token = getOrRefreshToken(code, wxid, null, null);
    } else {
      user_info = getUerInfoByStore(openid);
      if (Objects.nonNull(user_info)) {
        return user_info;
      }
      user_token = getAccessTokenAndOpenid(code, wxid, openid);
    }

    String user = restTemplate.getForObject(String.format(user_url, user_token.getString("access_token"), user_token.getString("openid")), String.class);
    try {
      user_info = new ObjectMapper().readValue(user, EntityBean.class);
      if (user_info.containsKey("errcode")) {
        throw new APIException(5001, user_info.getString("errmsg"));
      }
      weChatTokenCache.putWechatToken(7200, openid, user);
      return user_info;
    } catch (JsonProcessingException e) {
      throw new APIException(5001, "user 反序列化失败");
    }
  }

  private EntityBean getUerInfoByStore(String openid) {
    String userstr = weChatTokenCache.getWeChatToken(openid);
    if (!StringUtils.isEmpty(userstr)) {
      try {
        return new ObjectMapper().readValue(userstr, EntityBean.class);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override
  public boolean addWxConfig(String appid, String appsecret) {
    EntityBean cfg = new EntityBean();
    cfg.put("appid", appid);
    cfg.put("appsecret", appsecret);
    return weChatMapping.addWeChatCfg(cfg);
  }

  @Override
  public String autoAuth(String wxid) {
    return getAppidById(wxid);
  }

  /**
   * 通过页面授权获取的code, 进行用户信息访问token和刷新token以及openid的获取
   * <p>
   * 注意: scope的所用域
   * <p>
   * snsapi_base 不弹出授权页面，直接跳转，只能获取用户openid
   * <p>
   * snsapi_userinfo 弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息
   */
  private EntityBean getAccessTokenAndOpenid(String code, String wxid, String openid) {
    EntityBean openidAndAccessToken;
    String key1 = String.format("openid_%s_access", openid);
    String openid_access_token = weChatTokenCache.getWeChatToken(key1);
    if (StringUtils.isEmpty(openid_access_token)) {
      openidAndAccessToken = getUerTokenBean(code, wxid, openid, key1);
    } else {
      openidAndAccessToken = new EntityBean();
      openidAndAccessToken.put("access_token", openid_access_token);
      openidAndAccessToken.put("openid", openid);
    }
    return openidAndAccessToken;
  }

  private EntityBean getUerTokenBean(String code, String wxid, String openid, String key1) {
    String key2 = String.format("openid_%s_refresh", openid);
    String openid_refresh_token = weChatTokenCache.getWeChatToken(key2);
    if (StringUtils.isEmpty(openid_refresh_token)) {
      return getOrRefreshToken(code, wxid, key1, key2);
    } else {
      return getOrRefreshToken(openid_refresh_token, wxid, key1);
    }
  }

  /**
   * refresh_token没有过期, 刷新access_token
   */
  private EntityBean getOrRefreshToken(String refresh_token, String wxid, String key1) {
    String appid = getAppidById(wxid);
    String ret = restTemplate.getForObject(String.format(user_refresh_token_url, appid, refresh_token), String.class);
    try {
      EntityBean user_bean_token = new ObjectMapper().readValue(ret, EntityBean.class);
      if (user_bean_token.containsKey("errcode")) {
        throw new APIException(5001, user_bean_token.getString("errmsg"));
      }
      String acc_token = user_bean_token.getString("access_token");
      int expires = user_bean_token.getInt("expires_in");
      weChatTokenCache.putWechatToken(expires, key1, acc_token);
      return user_bean_token;
    } catch (JsonProcessingException e) {
      throw new APIException(5001, "获取用户授权访问token, 反序列化出错");
    }
  }

  /**
   * refresh_token过期, 重新获取access_token
   */
  private EntityBean getOrRefreshToken(String code, String wxid, String key1, String key2) {
    String appsecret = getAppsecretById(wxid);
    String appid = getAppidById(wxid);
    String ret = restTemplate.getForObject(String.format(user_token_url, appid, appsecret, code), String.class);
    try {
      EntityBean user_bean_token = new ObjectMapper().readValue(ret, EntityBean.class);
      if (user_bean_token.containsKey("errcode")) {
        throw new APIException(5001, user_bean_token.getString("errmsg"));
      }
      String acc_token = user_bean_token.getString("access_token");
      int expires = user_bean_token.getInt("expires_in");
      String refresh_token = user_bean_token.getString("refresh_token");
      String openid = user_bean_token.getString("openid");
      if (StringUtils.isEmpty(key1)) {
        key1 = String.format("openid_%s_access", openid);
      }
      if (StringUtils.isEmpty(key2)) {
        key2 = String.format("openid_%s_refresh", openid);
      }
      weChatTokenCache.putWechatToken(expires, key1, acc_token);
      weChatTokenCache.putWechatToken(3600 * 24 * 30, key2, refresh_token);// 缓存一个月
      return user_bean_token;
    } catch (JsonProcessingException e) {
      throw new APIException(5001, "获取用户授权访问token, 反序列化出错");
    }
  }

  /**
   * 获取公众号密钥
   *
   * @param wxid 存储的id
   * @return 密钥
   */
  private String getAppsecretById(String wxid) {
    if (storage.containsKey(wxid)) {
      return ((EntityBean) storage.get(wxid)).getString("appsecret");
    } else {
      EntityBean wechatcfg = weChatMapping.getWeChatCfgById(wxid);
      storage.put(wxid, wechatcfg);
      return wechatcfg.getString("appsecret");
    }
  }

  /**
   * 获取公众号id
   *
   * @param wxid 存储的id
   * @return 公众号id
   */
  private String getAppidById(String wxid) {
    if (storage.containsKey(wxid)) {
      return ((EntityBean) storage.get(wxid)).getString("appid");
    } else {
      EntityBean wechatcfg = weChatMapping.getWeChatCfgById(wxid);
      storage.put(wxid, wechatcfg);
      return wechatcfg.getString("appid");
    }
  }

  /**
   * 获取 access_token
   */
  private String getToken(String wxid) {
    String key = String.format("access_%s_token", wxid);
    String access_token = weChatTokenCache.getWeChatToken(key);
    if (StringUtils.isEmpty(access_token)) {
      String appsecret = getAppsecretById(wxid);
      String appid = getAppidById(wxid);
      EntityBean access_token_bean = restTemplate.getForObject(String.format(token_url, appid, appsecret), EntityBean.class);
      if (Objects.nonNull(access_token_bean) && access_token_bean.containsKey("access_token")) {
        int expire = access_token_bean.getInt("expires_in");
        access_token = access_token_bean.getString("access_token");
        weChatTokenCache.putWechatToken(expire, key, access_token);
      }
    }
    return access_token;
  }

  /**
   * 根据access_token获取临时(7200s)jsapi的票根, 用于JS-SDK使用权限签名算法
   */
  private String getTicket(String wxid) {
    String key = String.format("access_%s_ticket", wxid);
    String ticket = weChatTokenCache.getWeChatToken(key);
    if (StringUtils.isEmpty((ticket))) {
      String access_token = getToken(wxid);
      EntityBean ticket_bean = restTemplate.getForObject(String.format(ticket_url, access_token), EntityBean.class);
      if (Objects.nonNull(ticket_bean) && ticket_bean.containsKey("ticket")) {
        int expire = ticket_bean.getInt("expires_in");
        ticket = ticket_bean.getString("ticket");
        weChatTokenCache.putWechatToken(expire, key, ticket);
      }
    }
    return ticket;
  }
}
