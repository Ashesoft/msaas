package com.longrise.msaas.global.utils;

import org.slf4j.MDC;

public class BindLogClass{
    public static void put(String val){
        MDC.put("clazz", val);
    }
}
