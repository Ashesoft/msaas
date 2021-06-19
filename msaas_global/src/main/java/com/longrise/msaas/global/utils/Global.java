package com.longrise.msaas.global.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Global {
  private static Logger logger;

  private static Global ourInstance = new Global();

  public static Global getInstance() {
    logger = LoggerFactory.getLogger(MDC.get("clazz"));
    return ourInstance;
  }

  public void logInfo(String msg) {
    logger.info(msg);
  }

  @Deprecated
  public <T> T checkNull(T t, Class<? extends RuntimeException> clazz, String msg) {
    if (t == null) {
      try {
        Constructor<? extends RuntimeException> eCons = clazz.getDeclaredConstructor(String.class);
        eCons.setAccessible(true);
        throw eCons.newInstance(msg);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
        e.printStackTrace();
      }
    }
    return t;
  }
}
