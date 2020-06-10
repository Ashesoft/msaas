package com.longrise.msaas.global.domain;

public enum ResultStatus {
    SUCCESS(2000, "success"),
    BYZERO(3001, "除数不能为零"),
    FAILED(4001, "响应失败"),
    VALIDATE_FAILED(4002, "参数不合法"),
    HASNULL(4003, "空指针"),
    ERROR(5000, "未知错误");

    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    ResultStatus(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

}
