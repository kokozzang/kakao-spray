package com.wspark.kakaospray.spray.dto;

import lombok.Getter;

@Getter
public class PrizeMoneyResponse {

  private final int prizeMoney;

  public PrizeMoneyResponse(int prizeMoney) {
    this.prizeMoney = prizeMoney;
  }
}
