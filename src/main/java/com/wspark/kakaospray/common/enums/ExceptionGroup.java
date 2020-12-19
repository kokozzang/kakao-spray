package com.wspark.kakaospray.common.enums;

import lombok.Getter;

public enum ExceptionGroup {
  INTERNAL_SERVER("500","Internal Server Error"),
  BAD_REQUEST("400", "Invalid parameters"),
  UNAUTHORIZED("401", "Unauthorized"),
  FORBIDDEN("403", "forbidden"),
  NOT_FOUND("404", "Not Found"),
  METHOD_NOT_ALLOWED("405", "Requested method is not supported"),
  ;

  @Getter
  private String code;

  @Getter
  private String message;

  ExceptionGroup(String code, String message){
    this.code = code;
    this.message = message;
  }
}
