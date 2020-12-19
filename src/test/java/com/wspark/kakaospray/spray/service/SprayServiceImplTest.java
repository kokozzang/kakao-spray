package com.wspark.kakaospray.spray.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.wspark.kakaospray.spray.dto.PrizeMoneyResponse;
import com.wspark.kakaospray.spray.dto.SprayCreateRequest;
import com.wspark.kakaospray.spray.dto.SprayCreateResponse;
import com.wspark.kakaospray.spray.dto.SprayStatusResponse;
import com.wspark.kakaospray.spray.exception.AccessByOwnerException;
import com.wspark.kakaospray.spray.exception.AllPrizeMoneyPaidException;
import com.wspark.kakaospray.spray.exception.AlreadyPaidException;
import com.wspark.kakaospray.spray.exception.InvalidRoomException;
import com.wspark.kakaospray.spray.exception.InvalidTokenException;
import com.wspark.kakaospray.spray.exception.SprayExpiredException;
import com.wspark.kakaospray.spray.exception.SprayStatusExpiredException;
import com.wspark.kakaospray.spray.model.Spray;
import com.wspark.kakaospray.spray.model.SprayItem;
import com.wspark.kakaospray.spray.repository.SprayRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SprayServiceImplTest {

  @Mock
  private SprayRepository sprayRepository;

  @InjectMocks
  private SprayServiceImpl sprayService;

  String token;
  int budget;
  String roomId;
  int ownerUserId;
  int numberOfPeople;
  Spray spray;

  @BeforeEach
  void beforeEach() {
    token = "tkn";
    budget = 100;
    roomId = "ROOM_1";
    ownerUserId = 1;
    numberOfPeople = 3;

    SprayCreateRequest sprayCreateRequest = SprayCreateRequest.builder()
        .budget(budget)
        .numberOfPeople(numberOfPeople)
        .build();

    spray = Spray.of(ownerUserId, roomId, sprayCreateRequest);
    ReflectionTestUtils.setField(spray, "createdAt", LocalDateTime.now());
  }

  @AfterEach
  void afterEach() {

  }


  @Test
  void createSpray() {

    SprayCreateRequest sprayCreateRequest = SprayCreateRequest.builder()
        .budget(budget)
        .numberOfPeople(numberOfPeople)
        .build();
    // given
    given(sprayRepository.save(any(Spray.class)))
        .willReturn(spray);

    // when
    SprayCreateResponse sprayCreateResponse = sprayService.createSpray(ownerUserId, roomId, sprayCreateRequest);

    // then
    Assertions.assertThat(sprayCreateResponse.getToken()).isNotBlank();
  }

  @DisplayName("payPrizeMoney 성공")
  @Test
  void payPrizeMoney() {

    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.of(spray));

    // when
    PrizeMoneyResponse prizeMoneyResponse = sprayService.payPrizeMoney(token, 4, roomId);

    // then
    Assertions.assertThat(prizeMoneyResponse.getPrizeMoney()).isGreaterThan(0);

  }

  @DisplayName("payPrizeMoney 토큰 불일 예외")
  @Test
  void payPrizeMoney_InvalidTokenException() {
    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.empty());

    // when
    // then
    Assertions.assertThatExceptionOfType(InvalidTokenException.class)
        .isThrownBy(() -> sprayService.payPrizeMoney("wrong", 123, roomId));

  }

  @DisplayName("payPrizeMoney 본인 뿌리기 받기 예외")
  @Test
  void payPrizeMoney_AccessByOwnerException() {
    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.of(spray));

    // when
    // then
    Assertions.assertThatExceptionOfType(AccessByOwnerException.class)
        .isThrownBy(() -> sprayService.payPrizeMoney(token, ownerUserId, roomId));

  }

  @DisplayName("payPrizeMoney 대화방 아이디 불일치 예외")
  @Test
  void payPrizeMoney_InvalidRoomException() {
    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.of(spray));

    // when
    // then
    Assertions.assertThatExceptionOfType(InvalidRoomException.class)
        .isThrownBy(() -> sprayService.payPrizeMoney(token, 123, "GGANG"));

  }

  @DisplayName("payPrizeMoney 뿌리기 만료 예외")
  @Test
  void payPrizeMoney_SprayExpiredException() {
    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.of(spray));

    ReflectionTestUtils
        .setField(spray, "createdAt", LocalDateTime.now().minusMinutes(20));

    // when
    // then
    Assertions.assertThatExceptionOfType(SprayExpiredException.class)
        .isThrownBy(() -> sprayService.payPrizeMoney(token, 123, roomId));

  }

  @DisplayName("payPrizeMoney 지급 이력이 있는데 시도 예외")
  @Test
  void payPrizeMoney_AlreadyPaidException() {
    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.of(spray));

    // when
    sprayService.payPrizeMoney(token, 2, roomId);

    // then
    Assertions.assertThatExceptionOfType(AlreadyPaidException.class)
        .isThrownBy(() -> sprayService.payPrizeMoney(token, 2, roomId));
  }

  @DisplayName("payPrizeMoney 모두 지급 완료된 뿌리기 예외")
  @Test
  void payPrizeMoney_AllPrizeMoneyPaidException() {
    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.of(spray));

    // when
    // then
    Assertions.assertThatExceptionOfType(AllPrizeMoneyPaidException.class)
        .isThrownBy(() -> {
          for (int i = 0; i < numberOfPeople + 1; i++) {
            int userId = 1000 + i;
            sprayService.payPrizeMoney(token, userId, roomId);
          }
        });
  }

  @DisplayName("getSprayStatus 성공")
  @Test()
  void getSprayStatus() {

    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.of(spray));

    // when
    SprayStatusResponse sprayStatus = sprayService.getSprayStatus(token, ownerUserId);

    // then
    Assertions.assertThat(sprayStatus).isNotNull();
    Assertions.assertThat(sprayStatus.getBudget()).isEqualTo(spray.getBudget());

    int paidPrizeMoney = spray.getSprayItems().stream()
        .filter(SprayItem::isPaid)
        .mapToInt(SprayItem::getPrizeMoney)
        .sum();

    Assertions.assertThat(sprayStatus.getPaidPrizeMoney()).isEqualTo(paidPrizeMoney);
  }

  @DisplayName("getSprayStatus 토큰 불일치 예외")
  @Test()
  void getSprayStatus_InvalidTokenException() {

    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.empty());

    // when
    // then
    Assertions.assertThatExceptionOfType(InvalidTokenException.class)
        .isThrownBy(() -> sprayService.getSprayStatus(token, ownerUserId));
  }

  @DisplayName("getSprayStatus 뿌리기 조회 기간 만료 예외")
  @Test()
  void getSprayStatus_SprayStatusExpiredException() {

    // given
    given(sprayRepository.findByToken(anyString()))
        .willReturn(Optional.of(spray));
    ReflectionTestUtils
        .setField(spray, "createdAt", LocalDateTime.now().minusDays(10));

    // when
    // then
    Assertions.assertThatExceptionOfType(SprayStatusExpiredException.class)
        .isThrownBy(() -> sprayService.getSprayStatus(token, ownerUserId));
  }
}