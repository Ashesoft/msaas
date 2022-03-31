package com.longrise.msaas.global.domain;

import java.util.concurrent.*;

public class WeChatTokenCache {
  private  EntityBean tokens = new EntityBean();
  private  DelayQueue<WeChatToken> caches = new DelayQueue<>();

  public WeChatTokenCache() {
    Executors.newSingleThreadExecutor().execute(() -> {
      while (true) {
        try {
          WeChatToken weChatToken = this.caches.take();
          this.tokens.remove(weChatToken.getTokenKey());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }
  public String getWeChatToken(String tokenKeyType){
    if(this.tokens.containsKey(tokenKeyType)){
      return this.tokens.getString(tokenKeyType);
    }
    return null;
  }

  public void putWechatToken (int expire, String tokenKeyType, String tokenVal){
    this.tokens.put(tokenKeyType, tokenVal);
    this.caches.add(WeChatToken.getInstance(expire, tokenKeyType));
  }


  private static class WeChatToken implements Delayed{
    private long expire;
    private String tokenKeyType;

    private WeChatToken(long expire , String tokenKeyType){
      this.tokenKeyType = tokenKeyType;
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
      return this.tokenKeyType;
    }
  }
}
