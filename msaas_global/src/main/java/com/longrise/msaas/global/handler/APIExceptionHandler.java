package com.longrise.msaas.global.handler;

import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.ResultStatus;
import com.longrise.msaas.global.domain.ResultV0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class APIExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(APIException.class);

  @ExceptionHandler(Exception.class)
  public ResultV0<String> ExceptionHandler(HttpServletRequest r, Exception e) {

    logger.error(String.format("%s with %s", e.toString(), e.getStackTrace()[0]));

    // 注意哦，这里返回类型是自定义响应体
    ResultV0<String> rv0 = new ResultV0<>(ResultStatus.ERROR, r.getRequestURI(), e.getMessage());
    Class<?> clazz = e.getClass();
    if (clazz == APIException.class) {
      APIException ex = (APIException) e;
      rv0 = new ResultV0<>(ex.getStatus(), ex.getMsg(), r.getRequestURI(), e.getMessage());
    } else if (clazz == ArithmeticException.class) {
      rv0 = new ResultV0<>(ResultStatus.BYZERO, r.getRequestURI(), e.getMessage());
    } else if (clazz == NullPointerException.class || clazz == ArrayIndexOutOfBoundsException.class) {
      rv0 = new ResultV0<>(ResultStatus.HASNULL, r.getRequestURI(), e.getMessage());
    } else if (clazz == IllegalArgumentException.class || clazz == IllegalAccessException.class) {
      rv0 = new ResultV0<>(ResultStatus.VALIDATE_FAILED, r.getRequestURI(), e.getMessage());
    }
    return rv0;
  }

}
