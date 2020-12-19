package com.wspark.kakaospray.spray.dto;

import com.wspark.kakaospray.spray.model.Spray;
import com.wspark.kakaospray.spray.model.SprayItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SprayStatusResponse {

  // 뿌린 시각
  private final LocalDateTime createdAt;

  // 뿌린 금액
  private final Integer budget;

  // 받기 완료된 금액
  private final int paidPrizeMoney;

  // 받기 완료된 정보
  private final List<PaymentDetail> paymentDetails;

  public SprayStatusResponse(Spray spray) {
    this.createdAt = spray.getCreatedAt();
    this.budget = spray.getBudget();

    this.paidPrizeMoney = spray.getSprayItems().stream()
        .filter(SprayItem::isPaid)
        .mapToInt(SprayItem::getPrizeMoney)
        .sum();

    this.paymentDetails = spray.getSprayItems().stream()
        .filter(SprayItem::isPaid)
        .map(sprayItem -> new PaymentDetail(sprayItem.getPrizeMoney(), sprayItem.getUserId()))
        .collect(Collectors.toList());
  }

  @Getter
  @Builder
  @AllArgsConstructor
  private static class PaymentDetail {

    // 받은 금액
    private final int prizeMoney;

    // 받은 사용자 아이디
    private final int userId;

  }


}
