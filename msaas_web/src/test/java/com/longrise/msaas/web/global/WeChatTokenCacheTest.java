package com.longrise.msaas.web.global;

import com.longrise.msaas.global.domain.WeChatTokenCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeChatTokenCacheTest {

  @Test
  public void TestGetToken() {
    for (int i = 0; i < 30; i++) {
      String val1 = WeChatTokenCache.getInstance().getWeChatToken("a");
      String val2 = WeChatTokenCache.getInstance().getWeChatToken("b");
      if (Objects.isNull(val1)) {
        val1 = String.valueOf(Math.random());
        WeChatTokenCache.getInstance().putWechatToken(5, "a", val1);
      }
      if (Objects.isNull(val2)) {
        val2 = UUID.randomUUID().toString();
        WeChatTokenCache.getInstance().putWechatToken(10, "b", val2);
      }
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.out.println("出错了");
      }
      System.out.print("val1==>"+val1+"\t");
      System.out.println("val2==>"+val2);
    }
  }

}
