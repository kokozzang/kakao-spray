package com.wspark.kakaospray.spray.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SprayCreateRequest {

  // 뿌릴 금액
  @NotNull
  @Min(1)
  @Max(Integer.MAX_VALUE)
  private final Integer budget;

  // 뿌릴 인원 수
  @Min(1)
  @NotNull
  private final Integer numberOfPeople;

}
