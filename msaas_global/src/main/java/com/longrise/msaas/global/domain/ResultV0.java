package com.longrise.msaas.global.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 返回数据封装类
 * @param <T>
 */
public class ResultV0<T> {
    private String timestamp;
    private int status;
    private String path;
    private String msg;
    private T data;

    public ResultV0(T data) {
        this("*/*", data);
    }
    public ResultV0(String path, T data){
        this(ResultStatus.SUCCESS, path, data);
    }
    public ResultV0(ResultStatus resultCode, String path, T data){
        this(resultCode.getStatus(), resultCode.getMsg(), path, data);
    }

    public ResultV0(int status, String msg, String path, T data){
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        this.status = status;
        this.path = path;
        this.msg = msg;
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
