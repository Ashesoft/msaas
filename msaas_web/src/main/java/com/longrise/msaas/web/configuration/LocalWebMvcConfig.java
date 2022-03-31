package com.longrise.msaas.web.configuration;

import com.longrise.msaas.web.interceptor.AuthorizationInterceptor;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;
import java.util.Arrays;

@Configuration
public class LocalWebMvcConfig implements WebMvcConfigurer {
  @Value("${address.audio.intercept}")
  private String audioIntercept;
  @Value("${address.audio.redirect}")
  private String audioRedirect;

  @Autowired
  private Environment env; // springboot 环境
  @Autowired
  private JsonWebTokenConfig jsonWebTokenConfig; // jwt 配置类

  /**
   * 将服务器请求资源的地址映射到本地路径
   *
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(audioIntercept).addResourceLocations(audioRedirect);
    registry.addResourceHandler(env.getProperty("address.video.intercept")).addResourceLocations(env.getProperty("address.video.redirect"));
  }

  /**
   * 添加自定义的拦截机制
   *
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AuthorizationInterceptor(jsonWebTokenConfig)).addPathPatterns("/**");
  }

  // 生成加密/解密配置文件明文的bean
  @Bean(name = "codeSheepEncryptorBean")
  public StringEncryptor codesheepStringEncryptor() {

    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    // 通过使用jvm环境的方式隐蔽jasypr的加密密钥
    config.setPassword(System.getProperty("jasypt.encryptor.password"));
    config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    config.setStringOutputType("base64");
    encryptor.setConfig(config);

    return encryptor;
  }

  // 自定义缓存key
  @Bean("myKeyGenerator")
  public KeyGenerator keyGenerator() {
    return new KeyGenerator() {
      @Override
      public Object generate(Object target, Method method, Object... params) {
        return method.getName() + Arrays.toString(params);
      }
    };
  }
}
