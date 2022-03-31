package com.longrise.msaas.global.domain;

public class APIException extends RuntimeException {
  private int status;
  private String msg;

  public APIException() {
    this.status = 4004;
    this.msg = super.getMessage();
  }

  public APIException(String msg) {
    this(4004, msg);
  }

  public APIException(int status, String msg) {
    super(msg);
    this.status = status;
    this.msg = msg;
  }

  public int getStatus() {
    return status;
  }

  public String getMsg() {
    return msg;
  }
}
