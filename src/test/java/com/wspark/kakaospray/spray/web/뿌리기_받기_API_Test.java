package com.wspark.kakaospray.spray.web;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wspark.kakaospray.spray.dto.PrizeMoneyResponse;
import com.wspark.kakaospray.spray.exception.AccessByOwnerException;
import com.wspark.kakaospray.spray.exception.AlreadyPaidException;
import com.wspark.kakaospray.spray.exception.InvalidRoomException;
import com.wspark.kakaospray.spray.exception.InvalidTokenException;
import com.wspark.kakaospray.spray.exception.SprayExpiredException;
import com.wspark.kakaospray.spray.service.SprayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(SprayController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("뿌리기 받기 API 테스트")
@Slf4j
class 뿌리기_받기_API_Test {

  @MockBean
  private SprayService sprayService;

  @Autowired
  private MockMvc mockMvc;

  private HttpHeaders httpHeaders;

  PrizeMoneyResponse prizeMoneyResponse;

  @BeforeEach
  void beforeEach() {
    httpHeaders = new HttpHeaders();
    httpHeaders.add("X-USER-ID", "1");
    httpHeaders.add("X-ROOM-ID", "ROOM-1");

    prizeMoneyResponse = new PrizeMoneyResponse(10);

  }

  @DisplayName("201: 뿌리기 받기 성공")
  @Test
  void payPrizeMoney() throws Exception {
    // given
    given(sprayService.payPrizeMoney(anyString(), anyInt(), anyString()))
        .willReturn(prizeMoneyResponse);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray/tkn/prize-money")
        .headers(httpHeaders)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8");

    // when
    ResultActions resultActions = mockMvc.perform(requestBuilder)
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isCreated());
  }

  @DisplayName("401: 유효하지 않은 토큰")
  @Test
  void payPrizeMoney_InvalidTokenException() throws Exception {
    // given
    given(sprayService.payPrizeMoney(anyString(), anyInt(), anyString()))
        .willThrow(InvalidTokenException.class);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray/tkn/prize-money")
        .headers(httpHeaders)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8");

    // when
    ResultActions resultActions = mockMvc.perform(requestBuilder)
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isUnauthorized());
  }

  @DisplayName("403: 자신의 뿌리기 받기 시도")
  @Test
  void payPrizeMoney_AccessByOwnerException() throws Exception {
    // given
    given(sprayService.payPrizeMoney(anyString(), anyInt(), anyString()))
        .willThrow(AccessByOwnerException.class);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray/tkn/prize-money")
        .headers(httpHeaders)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8");

    // when
    ResultActions resultActions = mockMvc.perform(requestBuilder)
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isForbidden());
  }

  @DisplayName("401: 다른 대화방에서 시도")
  @Test
  void payPrizeMoney_InvalidRoomException() throws Exception {
    // given
    given(sprayService.payPrizeMoney(anyString(), anyInt(), anyString()))
        .willThrow(InvalidRoomException.class);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray/tkn/prize-money")
        .headers(httpHeaders)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8");

    // when
    ResultActions resultActions = mockMvc.perform(requestBuilder)
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isUnauthorized());
  }

  @DisplayName("403: 만료된 뿌리기 받기")
  @Test
  void payPrizeMoney_SprayExpiredException() throws Exception {
    // given
    given(sprayService.payPrizeMoney(anyString(), anyInt(), anyString()))
        .willThrow(SprayExpiredException.class);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray/tkn/prize-money")
        .headers(httpHeaders)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8");

    // when
    ResultActions resultActions = mockMvc.perform(requestBuilder)
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isForbidden());
  }

  @DisplayName("403: 지급된 이력이 있음")
  @Test
  void payPrizeMoney_AlreadyPaidException() throws Exception {
    // given
    given(sprayService.payPrizeMoney(anyString(), anyInt(), anyString()))
        .willThrow(AlreadyPaidException.class);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray/tkn/prize-money")
        .headers(httpHeaders)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8");

    // when
    ResultActions resultActions = mockMvc.perform(requestBuilder)
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isForbidden());
  }


}