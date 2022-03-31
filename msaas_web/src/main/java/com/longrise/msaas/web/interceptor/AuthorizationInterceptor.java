package com.longrise.msaas.web.interceptor;

import com.longrise.msaas.global.annotation.LoginToken;
import com.longrise.msaas.global.annotation.PassToken;
import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.web.configuration.JsonWebTokenConfig;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 自定义的拦截机制--用户鉴权
 */
public class AuthorizationInterceptor implements HandlerInterceptor {
  private JsonWebTokenConfig jsonWebTokenConfig;

  public AuthorizationInterceptor(JsonWebTokenConfig jsonWebTokenConfig) {
    this.jsonWebTokenConfig = jsonWebTokenConfig;
  }


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getHeader("Authorization"); // 从请求头中取出token
    if (!(handler instanceof HandlerMethod)) { // 如果不是映射到的是方法, 直接通过
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();
    // 检查方法上是否有passToken注解, 有则跳过认证
    if (method.isAnnotationPresent(PassToken.class)) {
      PassToken passToken = method.getAnnotation(PassToken.class);
      if (passToken.required()) {
        return true;
      }
    }
    if (method.isAnnotationPresent(LoginToken.class)) {
      LoginToken loginToken = method.getAnnotation(LoginToken.class);
      if (loginToken.required()) {
        if (token == null) {
          throw new APIException(5000, "没有token, 请登录");
        }
        if (this.jsonWebTokenConfig.isExpired(token)) {
          throw new APIException(5000, "授权已过期, 请重新登录");
        }
      }
    }
    return true;
  }
}
