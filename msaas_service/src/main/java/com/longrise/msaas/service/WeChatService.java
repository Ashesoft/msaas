package com.longrise.msaas.service;

import com.longrise.msaas.global.domain.EntityBean;

public interface WeChatService {
  /**
   * 获取微信api的访问签名
   *
   * @param wxid 公众号配置id
   * @param url  当前访问公众号api的网页地址
   * @return 返回访问token
   */
  EntityBean signature(String wxid, String url);

  /**
   * 获取当前公众号用户信息
   *
   * @param code   通过用户授权获取到的code
   * @param wxid   当前公众号id
   * @param openid 用户与当前公众号关联的id
   * @return 返回当前公众号用户信息
   */
  EntityBean getWxUserInfo(String code, String wxid, String openid);

  boolean addWxConfig(String appid, String appsecret);

  String autoAuth(String wxid);
}
