package com.longrise.msaas.global.domain;

import java.util.Objects;
import java.util.concurrent.*;

public class WeChatTokenCache {
  private static EntityBean tokens = new EntityBean();
  private static DelayQueue<WeChatToken> caches = new DelayQueue<>();
  private static WeChatTokenCache instance;

  private WeChatTokenCache(){}

  public static WeChatTokenCache getInstance() {
    if(Objects.isNull(instance)){
      synchronized (WeChatTokenCache.class){
        if(Objects.isNull(instance)){
          Executors.newSingleThreadExecutor().execute(()->{
            while (true){
              try {
                WeChatToken weChatToken = caches.take();
                tokens.remove(weChatToken.getTokenKey());
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          });
          instance = new WeChatTokenCache();
          return instance;
        }
      }
    }
    return instance;
  }

  public String getWeChatToken(String tokenKey){
    if(tokens.containsKey(tokenKey)){
      return tokens.getString(tokenKey);
    }
    return null;
  }

  public void putWechatToken (int expire, String tokenKey, String tokenVal){
    tokens.put(tokenKey, tokenVal);
    caches.add(WeChatToken.getInstance(expire, tokenKey));
  }


  private static class WeChatToken implements Delayed{
    private long expire;
    private String tokenKey;

    private WeChatToken(long expire , String tokenKey){
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
