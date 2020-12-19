package com.wspark.kakaospray.spray.service;

import com.wspark.kakaospray.spray.dto.PrizeMoneyResponse;
import com.wspark.kakaospray.spray.dto.SprayCreateRequest;
import com.wspark.kakaospray.spray.dto.SprayCreateResponse;
import com.wspark.kakaospray.spray.dto.SprayStatusResponse;
import com.wspark.kakaospray.spray.model.Spray;

public interface SprayService {

  /**
   * 뿌리기 생성하기
   * @param userId
   * @param roomId
   * @param sprayCreateRequest
   * @return 뿌리기 생성 응답 객체
   */
  SprayCreateResponse createSpray(Integer userId, String roomId, SprayCreateRequest sprayCreateRequest);

  /**
   * 뿌리기 받기
   * @param token
   * @param userId
   * @param roomId
   * @return 뿌리기 받기 응답 객체
   */
  PrizeMoneyResponse payPrizeMoney(String token, Integer userId, String roomId);

  /**
   * 뿌리기 상태 조회 하기
   * @param token
   * @param userId
   * @return 뿌리기 상태 조회 응답 객체
   */
  SprayStatusResponse getSprayStatus(String token, Integer userId);

}
