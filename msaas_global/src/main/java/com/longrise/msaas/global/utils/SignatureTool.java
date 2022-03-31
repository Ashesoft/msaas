package com.longrise.msaas.global.utils;

import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.EntityBean;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public final class SignatureTool {

  public static EntityBean sign(String jsapi_ticket, String url) {
    String nonce_str = create_nonce_str();
    long timestamp = create_timestamp();
    //注意这里参数名必须全部小写，且必须有序
    String string1 = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";
    String signature;
    string1 = String.format(string1, jsapi_ticket, nonce_str, timestamp, url);
    System.out.println(string1);
    try {
      MessageDigest crypt = MessageDigest.getInstance("SHA-1");
      crypt.reset();
      crypt.update(string1.getBytes(StandardCharsets.UTF_8));
      signature = byteToHex(crypt.digest());
    } catch (NoSuchAlgorithmException e) {
      throw new APIException(5000, "加盐失败");
    }
    EntityBean ret = new EntityBean();
    ret.put("url", url);
    ret.put("jsapi_ticket", jsapi_ticket);
    ret.put("nonceStr", nonce_str);
    ret.put("timestamp", timestamp);
    ret.put("signature", signature);
    return ret;
  }

  /**
   * 把加言后的字节数组转换成16进制
   *
   * @param hash 要加言的字节数组
   */
  private static String byteToHex(final byte[] hash) {
    try (Formatter formatter = new Formatter()) {
      for (byte b : hash) {
        formatter.format("%02x", b);
      }
      return formatter.toString();
    }
  }

  /**
   * 生成随机字符串
   */
  private static String create_nonce_str() {
    return RundomUid.get32BitUid();
  }

  /**
   * 获取当前时间的总秒数
   */
  public static long create_timestamp() {
    return System.currentTimeMillis() / 1000;
  }
}
