package com.longrise.msaas.global.utils;

import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

// 没有趋势递增效果, 不利于索引, 推荐使用 IdWorker 类
public class RundomUid {
  private static AtomicInteger Guid = new AtomicInteger(100);

  /**
   * 生成32位字母加数字的唯一ID
   */
  public static String get32BitUid() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  /**
   * 生成20位唯一纯数字ID, 4位年份+13位时间戳+3位自增数
   */
  public static String get20BItUid() {
    Guid.getAndIncrement();
    long now = System.currentTimeMillis();

    //获取开头4位年份数字
    String yyyy = new SimpleDateFormat("yyyy").format(now);

    //获取时间戳
    String info = Long.toString(now);

    /**
     * 获取末尾的三位随机数
     * int ran = (int)((Math.random()*9+1)*100);
     * 要是一段时间内的数据过大会有重复的情况, 故做以下修改
     */

    if (Guid.get() > 999) {
      Guid.set(100);
    }
    return yyyy + info + Guid.get();
  }
}
