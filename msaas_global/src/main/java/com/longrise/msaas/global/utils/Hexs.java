package com.longrise.msaas.global.utils;

import org.springframework.util.StringUtils;

import java.util.Optional;

public final class Hexs {
  private Hexs() {
  }

  /**
   * 16进制字符串转换成普通字符串(解码)
   *
   * @param src 16进制字符串
   * @return 转换后的普通字符串
   */
  public static String hexToString(String src) {
    return Optional.of(src)
      .filter(StringUtils::isEmpty)
      .orElseGet(() -> {
        String[] hexary = src.split("-");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : hexary) {
          stringBuilder.append((char) Integer.parseInt(s, 16));
        }
        return stringBuilder.toString();
      });
  }

  /**
   * 普通字符串转换成16进制字符串(编码)
   *
   * @param src 普通字符串
   * @return 转换后的16进制字符串
   */
  public static String stringToHex(String src) {
    return Optional.of(src)
      .filter(StringUtils::isEmpty)
      .orElseGet(() -> {
        int len = src.length();
        String[] hexary = new String[len];
        for (int i = 0; i < len; i++) {
          hexary[i] = Integer.toHexString(src.charAt(i));
        }
        return String.join("-", hexary);
      });
  }
}
