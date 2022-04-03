package com.longrise.msaas.global.domain;


import com.longrise.msaas.global.handler.WeChatCacheHandler;

import java.util.Objects;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WeChatCache {
  private EntityBean tokens = new EntityBean();
  private DelayQueue<WeChatToken> caches = new DelayQueue<>();
  private WeChatCacheHandler weChatCacheHandler;

  public WeChatCache(WeChatCacheHandler weChatCacheHandler) {
    this.weChatCacheHandler = weChatCacheHandler;
    Executors.newSingleThreadExecutor().execute(() -> {
      while (true) {
        try {
          WeChatToken weChatToken = caches.take();
          String tokenkey = weChatToken.getTokenKey();
          tokens.remove(tokenkey);
          EntityBean bean = new EntityBean();
          bean.put("beanname", "wechatcache");
          bean.put("tokenkey", tokenkey);
          bean.delete();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public String getWeChatToken(String tokenKey) {
    if (tokens.containsKey(tokenKey)) {
      return tokens.getString(tokenKey);
    }
    EntityBean tokenBean = weChatCacheHandler.getWeChatToken(tokenKey);
    if (Objects.isNull(tokenBean)) {
      return null;
    }
    long expire = tokenBean.getLong("expire");
    String tokenval = tokenBean.getString("tokenval");
    if (expire > System.currentTimeMillis()) {
      return tokenval;
    } else {
      EntityBean bean = new EntityBean();
      bean.put("beanname", "wechatcache");
      bean.put("tokenkey", tokenKey);
      bean.delete();
    }
    return null;
  }

  public void putWechatToken(int expire, String tokenKey, String tokenVal) {
    tokens.put(tokenKey, tokenVal);
    weChatCacheHandler.addWeChatToken(expire, tokenKey, tokenVal);
    caches.add(WeChatToken.getInstance(expire, tokenKey));
  }


  private static class WeChatToken implements Delayed {
    private long expire;
    private String tokenKey;

    private WeChatToken(long expire, String tokenKey) {
      this.tokenKey = tokenKey;
      this.expire = expire;
    }

    public static WeChatToken getInstance(int expire, String key) {
      long extime = System.currentTimeMillis() + expire * 1000;
      return new WeChatToken(extime, key);
    }

    @Override
    public long getDelay(TimeUnit unit) {
      long diff = this.getExpire() - System.currentTimeMillis();
      return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
      boolean bool = this.getDelay(TimeUnit.MILLISECONDS) >= o.getDelay(TimeUnit.MILLISECONDS);
      return bool ? 1 : -1;
    }

    public long getExpire() {
      return this.expire;
    }

    public String getTokenKey() {
      return this.tokenKey;
    }
  }
}
