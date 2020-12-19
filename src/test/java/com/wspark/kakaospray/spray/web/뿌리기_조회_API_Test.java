package com.wspark.kakaospray.spray.web;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wspark.kakaospray.common.util.StringUtil;
import com.wspark.kakaospray.spray.dto.PrizeMoneyResponse;
import com.wspark.kakaospray.spray.dto.SprayCreateRequest;
import com.wspark.kakaospray.spray.dto.SprayStatusResponse;
import com.wspark.kakaospray.spray.exception.InvalidTokenException;
import com.wspark.kakaospray.spray.exception.NotSprayOwnerException;
import com.wspark.kakaospray.spray.model.Spray;
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
class 뿌리기_조회_API_Test {

  @MockBean
  private SprayService sprayService;

  private ObjectMapper objectMapper = new ObjectMapper();

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

  @DisplayName("200: 뿌리기 조회 성공")
  @Test
  void payPrizeMoney() throws Exception {
//    Spray.builder()
    Integer userId = Integer.valueOf(httpHeaders.get("X-USER-ID").get(0));
    String roomId = httpHeaders.get("X-ROOM-ID").get(0);

    SprayCreateRequest sprayCreateRequest = SprayCreateRequest.builder()
        .budget(100)
        .numberOfPeople(4)
        .build();


    Spray spray = Spray.of(userId, roomId, sprayCreateRequest);

    SprayStatusResponse sprayStatusResponse = new SprayStatusResponse(spray);

    // given
    given(sprayService.getSprayStatus(anyString(), anyInt()))
        .willReturn(sprayStatusResponse);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/spray/tkn")
        .headers(httpHeaders)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8");

    // when
    ResultActions resultActions = mockMvc.perform(requestBuilder)
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isOk());
  }

  @DisplayName("401: 유효하지 않은 토큰")
  @Test
  void payPrizeMoney_InvalidTokenException() throws Exception {

    // given
    given(sprayService.getSprayStatus(anyString(), anyInt()))
        .willThrow(InvalidTokenException.class);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/spray/tkn")
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

  @DisplayName("403: 뿌리기 소유자가 아님")
  @Test
  void payPrizeMoney_NotSprayOwnerException() throws Exception {

    // given
    given(sprayService.getSprayStatus(anyString(), anyInt()))
        .willThrow(NotSprayOwnerException.class);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/spray/tkn")
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

  @DisplayName("403: 뿌리기 조회기간 만료")
  @Test
  void payPrizeMoney_SprayStatusExpiredException() throws Exception {

    // given
    given(sprayService.getSprayStatus(anyString(), anyInt()))
        .willThrow(NotSprayOwnerException.class);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/spray/tkn")
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