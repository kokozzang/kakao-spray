package com.wspark.kakaospray.common.dto;

import lombok.Getter;

@Getter
public class SuccessResponse extends Response {

  private Object data;

  public SuccessResponse(Object data) {
    this.code = "0000";
    this.message = "SUCCESS";
    this.data = data;
  }
}
