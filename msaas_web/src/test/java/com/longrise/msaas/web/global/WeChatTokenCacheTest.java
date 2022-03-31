package com.longrise.msaas.web.global;

import com.longrise.msaas.global.domain.WeChatTokenCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeChatTokenCacheTest {

  @Autowired
  private WeChatTokenCache weChatTokenCache;

  @Test
  public void TestGetToken() {
    for (int i = 0; i < 30; i++) {
      String val1 = weChatTokenCache.getWeChatToken("access_token");
      String val2 = weChatTokenCache.getWeChatToken("ticket");
      if (Objects.isNull(val1)) {
        val1 = String.valueOf(Math.random());
        weChatTokenCache.putWechatToken(5, "access_token", val1);
      }
      if (Objects.isNull(val2)) {
        val2 = UUID.randomUUID().toString();
        weChatTokenCache.putWechatToken(10, "ticket", val2);
      }
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.out.println("出错了");
      }
      System.out.print("val1==>" + val1 + "\t");
      System.out.println("val2==>" + val2);
    }
  }

}
