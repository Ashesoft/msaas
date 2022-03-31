package com.longrise.msaas.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.longrise.msaas"})
@EnableCaching // 默认使用了 SimpleCacheConfiguration 配置的缓存
//@EnableAsync // 开启异步注解 @Async
//@EnableScheduling // 开启定时任务注解 @Scheduled + cron 表达式
public class MsaasWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsaasWebApplication.class, args);
  }

}
