package com.wspark.kakaospray.spray.dto;

import lombok.Getter;

@Getter
public class SprayCreateResponse {

  private String token;

  public SprayCreateResponse(String token) {
    this.token = token;
  }
}
