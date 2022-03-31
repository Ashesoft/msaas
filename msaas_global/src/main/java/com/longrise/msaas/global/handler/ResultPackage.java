package com.longrise.msaas.global.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.ResultV0;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 对返回的数据进行统一包装处理理
 */
@RestControllerAdvice(basePackages = "com.longrise.msaas.web.controller") // 添加需要扫描的包
public class ResultPackage implements ResponseBodyAdvice<Object> {
  @Override
  public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
    // 如果接口返回的类型本身就是ResultV0那就是没有必要进行额外的操作, 返回false
    return !methodParameter.getParameterType().equals(ResultV0.class);
  }

  @Override
  public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<?
    extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                ServerHttpResponse serverHttpResponse) {
    // String 类型不能直接包装, 所以要进行特别的处理
    if (methodParameter.getGenericParameterType().equals(String.class)) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        return objectMapper.writeValueAsString(new ResultV0<>(o));
      } catch (JsonProcessingException e) {
        throw new APIException("返回String类型错误");
      }
    }
    return new ResultV0<>(o);
  }

  /**
   * 判断是否是Ajax方式请求的
   *
   * @param request {@link HttpServletRequest} 对象
   * @return true or false
   */
  @Deprecated
  private boolean isAjax(HttpServletRequest request) {
    String resquestType = request.getHeader("X-Requested-With");
    return "XMLHttpRequest".equals(resquestType);
  }
}
