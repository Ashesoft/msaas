package com.longrise.msaas.global.utils;

import com.longrise.msaas.global.domain.EntityBean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SignatureTool {

  public static final String getRandomChar(int len) {
    if (len < 1 || len > 36) {
      len = 18;
    }
    String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < len; i++) {
      random.nextInt(len);
    }
    return stringBuilder.toString();
  }

  /**
   * 获取当前时间的总秒数
   * @return
   */
  public static final long getCurrentTimeSeconds() {
    return System.currentTimeMillis() / 1000l;
  }

  /**
   *
   * @param bean
   * @return
   */
  public static final String sortASCII(EntityBean bean) {
    if (Objects.nonNull(bean) && !bean.isEmpty()) {
      Set<?> keySet = bean.keySet();
      int size = keySet.size();
      String[] keyArry = keySet.toArray(new String[size]);
      Arrays.sort(keyArry);
      StringBuilder stringBuilder = new StringBuilder();
      String value = null;
      for (int b = 0; b < size; b++) {
        String k = keyArry[b];
        value = bean.getString(k);
        if (Objects.nonNull(value)) {
          stringBuilder.append("&").append(k).append("=").append(value);
        }
      }
      return stringBuilder.substring(1);
    }
    return null;
  }

  public static void main(String[] args) {
    Map<String, String> map = new ConcurrentHashMap<>();
    map.put("aa", "111");
    map.put("zz", "000");
    map.put("vv", "888");
    map.put("ddd", "444");
    Set<String> set = map.keySet();
    System.out.println(set);
  }
}
