package com.longrise.msaas.global.utils;

import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.EntityBean;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;

public final class SignatureTool {

  public static EntityBean sign(String jsapi_ticket, String url) {
    String nonce_str = create_nonce_str();
    long timestamp = create_timestamp();
    //注意这里参数名必须全部小写，且必须有序
    String targetTxt = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";
    String signature;
    targetTxt = String.format(targetTxt, jsapi_ticket, nonce_str, timestamp, url);
    System.out.println(targetTxt);
    signature = getSignature(targetTxt);
    EntityBean ret = new EntityBean();
    ret.put("url", url);
    ret.put("noncestr", nonce_str);
    ret.put("timestamp", timestamp);
    ret.put("signature", signature);
    return ret;
  }

  public static String sign(String token, String timestamp, String nonce) {
    String[] array = new String[]{token, timestamp, nonce};
    StringBuilder stringBuilder = new StringBuilder();
    // 字符串排序
    Arrays.sort(array);
    String str = String.join("", array);
    return getSignature(str);
  }

  /**
   * 对目标内容使用 sha-1 进行加密
   *
   * @param targetTxt 要进行加密的目标内容
   * @return 加密后的结果
   */
  private static String getSignature(String targetTxt) {
    String signature;
    try {
      MessageDigest crypt = MessageDigest.getInstance("SHA-1");
      crypt.reset();
      crypt.update(targetTxt.getBytes(StandardCharsets.UTF_8));
      signature = byteToHex(crypt.digest());
    } catch (NoSuchAlgorithmException e) {
      throw new APIException(5000, "加言失败");
    }
    return signature;
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
