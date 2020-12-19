package com.wspark.kakaospray.spray.web;

import com.wspark.kakaospray.spray.dto.PrizeMoneyResponse;
import com.wspark.kakaospray.spray.dto.SprayCreateRequest;
import com.wspark.kakaospray.spray.dto.SprayCreateResponse;
import com.wspark.kakaospray.spray.dto.SprayStatusResponse;
import com.wspark.kakaospray.spray.service.SprayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/spray")
@RestController
@RequiredArgsConstructor
public class SprayController {

  private final SprayService sprayService;


  /**
   * 뿌리기 생성 API
   * 뿌리기가 생성되면 토큰을 발급한다.
   * @param userId 사용자 아이디 헤더
   * @param roomId 대화방 아이디 헤더
   * @param sprayCreateRequest request body
   * @return
   */
  @PostMapping(value = "")
  public ResponseEntity<SprayCreateResponse> createSpray(
      @RequestHeader("X-USER-ID") Integer userId,
      @RequestHeader("X-ROOM-ID") String roomId,
      @Validated @RequestBody SprayCreateRequest sprayCreateRequest) {

    SprayCreateResponse sprayCreateResponse = sprayService.createSpray(userId, roomId, sprayCreateRequest);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(sprayCreateResponse);
  }

  /**
   * 뿌리기 받기 API
   * @param userId
   * @param roomId
   * @param token 토큰
   * @return
   */
  @PostMapping(value = "/{token}/prize-money")
  public ResponseEntity<PrizeMoneyResponse> payPrizeMoney(
      @RequestHeader("X-USER-ID") Integer userId,
      @RequestHeader("X-ROOM-ID") String roomId,
      @PathVariable String token) {

    PrizeMoneyResponse prizeMoneyResponse = sprayService.payPrizeMoney(token, userId, roomId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(prizeMoneyResponse);
  }

  /**
   * 뿌리기 상태 조회 API
   * @param userId
   * @param roomId
   * @param token
   * @return
   */
  @GetMapping("/{token}")
  public ResponseEntity<SprayStatusResponse> sprayStatus(
      @RequestHeader("X-USER-ID") Integer userId,
      @RequestHeader("X-ROOM-ID") String roomId,
      @PathVariable String token) {

    SprayStatusResponse sprayStatus = sprayService.getSprayStatus(token, userId);

    return ResponseEntity.ok(sprayStatus);

  }


}
