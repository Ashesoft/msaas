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


  String addWxConfig(String appid, String appsecret);

  /**
   * 用户授权
   *
   * @param wxid 向系统添加微信配置时生成的id;
   * @return 重定向的链接
   */
  String autoAuth(String wxid);

  /**
   * 通过检验signature对请求进行校验, 以此来验证消息确实来自微信服务器
   * <br>
   * 1. 将token、timestamp、nonce三个参数进行字典序排序
   * <br>
   * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
   * <br>
   * 3. 获得加密后的字符串可与signature对比，标识该请求来源于微信,
   *
   * @param signature 微信加密签名，signature结合了在微信公众平台填写的token参数和请求中的timestamp参数、nonce参数。
   * @param timestamp 时间戳
   * @param nonce     随机数
   * @param echostr   随机字符串
   * @return 如果确认此次GET请求来自微信服务器, 原样返回echostr参数内容, 则接入生效, 成为开发者成功, 否则接入失败
   */
  String checkSign(String signature, String timestamp, String nonce, String echostr);
}
