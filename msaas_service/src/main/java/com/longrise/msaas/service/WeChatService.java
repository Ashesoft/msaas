package com.longrise.msaas.service;

import com.longrise.msaas.global.domain.EntityBean;

public interface WeChatService {
  /**
   * 获取微信api的访问token
   * @param url 当前访问微信api的网页地址
   * @return 返回访问token
   */
  EntityBean getAccessToken(String url);

  /**
   * 获取当前微信用户信息
   * @param code 通过appid获取到的code
   * @return 返回当前微信用户信息
   */
  EntityBean getWxUserInfo(String code);

  /**
   * 获取appid
   * @return appid
   */
  String getAppid();
}
