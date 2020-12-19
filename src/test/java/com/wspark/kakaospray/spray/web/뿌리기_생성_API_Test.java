package com.wspark.kakaospray.spray.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wspark.kakaospray.spray.dto.SprayCreateRequest;
import com.wspark.kakaospray.spray.dto.SprayCreateResponse;
import com.wspark.kakaospray.spray.service.SprayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
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
@DisplayName("뿌리기 생성 API 테스트")
@Slf4j
class 뿌리기_생성_API_Test {

  @MockBean
  private SprayService sprayService;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  private HttpHeaders httpHeaders;

  private SprayCreateRequest sprayCreateRequest;

  private SprayCreateResponse sprayCreateResponse;


  @BeforeEach
  void beforeEach() {
    httpHeaders = new HttpHeaders();
    httpHeaders.add("X-USER-ID", "1");
    httpHeaders.add("X-ROOM-ID", "ROOM-1");


    sprayCreateRequest = SprayCreateRequest.builder()
        .budget(100)
        .numberOfPeople(5)
        .build();

    sprayCreateResponse = new SprayCreateResponse("tkn");
  }

  @DisplayName("201: 뿌리기 생성 성공")
  @Test
  void createSpray() throws Exception {
    // given
    given(sprayService.createSpray(anyInt(), anyString(), any(SprayCreateRequest.class)))
        .willReturn(sprayCreateResponse);


    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray")
        .headers(httpHeaders)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8")
        .content(objectMapper.writeValueAsBytes(sprayCreateRequest));

    // when
    ResultActions resultActions = mockMvc.perform(requestBuilder)
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isCreated());
  }

  @DisplayName("400 밸리데이션: 헤더")
  @Nested
  class Header {

    @Test
    void EMPTY_USER_헤더() throws Exception {
      httpHeaders.remove("X-USER-ID");

      // given
      given(sprayService.createSpray(anyInt(), anyString(), any(SprayCreateRequest.class)))
          .willReturn(sprayCreateResponse);

      RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray")
          .headers(httpHeaders)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .content(objectMapper.writeValueAsBytes(sprayCreateRequest));

      // when
      ResultActions resultActions = mockMvc.perform(requestBuilder)
          .andDo(print());

      // then
      resultActions
          .andExpect(status().isBadRequest());
    }

    @Test
    void EMPTY_ROOM_헤더() throws Exception {
      httpHeaders.remove("X-ROOM-ID");

      SprayCreateResponse sprayCreateResponse = new SprayCreateResponse("tkn");

      // given
      given(sprayService.createSpray(anyInt(), anyString(), any(SprayCreateRequest.class)))
          .willReturn(sprayCreateResponse);

      RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray")
          .headers(httpHeaders)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .content(objectMapper.writeValueAsBytes(sprayCreateRequest));

      // when
      ResultActions resultActions = mockMvc.perform(requestBuilder)
          .andDo(print());

      // then
      resultActions
          .andExpect(status().isBadRequest());
    }


  }

  @DisplayName("400 밸리데이션: numberOfPeople")
  @Nested
  class NumberOfPeople {

    @Test
    void Null_이다() throws Exception {
      sprayCreateRequest = SprayCreateRequest.builder()
          .budget(sprayCreateRequest.getBudget())
          .numberOfPeople(null)
          .build();

      // given
      given(sprayService.createSpray(anyInt(), anyString(), any(SprayCreateRequest.class)))
          .willReturn(sprayCreateResponse);


      RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray")
          .headers(httpHeaders)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .content(objectMapper.writeValueAsBytes(sprayCreateRequest));

      // when
      ResultActions resultActions = mockMvc.perform(requestBuilder)
          .andDo(print());

      // then
      resultActions
          .andExpect(status().isBadRequest());
    }

    @Test
    void Min_보다_작음() throws Exception {
      sprayCreateRequest = SprayCreateRequest.builder()
          .budget(sprayCreateRequest.getBudget())
          .numberOfPeople(0)
          .build();

      // given
      given(sprayService.createSpray(anyInt(), anyString(), any(SprayCreateRequest.class)))
          .willReturn(sprayCreateResponse);


      RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray")
          .headers(httpHeaders)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .content(objectMapper.writeValueAsBytes(sprayCreateRequest));

      // when
      ResultActions resultActions = mockMvc.perform(requestBuilder)
          .andDo(print());

      // then
      resultActions
          .andExpect(status().isBadRequest());
    }
  }

  @DisplayName("400 밸리데이션: budget")
  @Nested
  class Budget {

    @Test
    void Min_보다_작음() throws Exception {
      sprayCreateRequest = SprayCreateRequest.builder()
          .budget(0)
          .numberOfPeople(sprayCreateRequest.getNumberOfPeople())
          .build();

      // given
      given(sprayService.createSpray(anyInt(), anyString(), any(SprayCreateRequest.class)))
          .willReturn(sprayCreateResponse);


      RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray")
          .headers(httpHeaders)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .content(objectMapper.writeValueAsBytes(sprayCreateRequest));

      // when
      ResultActions resultActions = mockMvc.perform(requestBuilder)
          .andDo(print());

      // then
      resultActions
          .andExpect(status().isBadRequest());
    }

    @Test
    void Null_이다() throws Exception {
      sprayCreateRequest = SprayCreateRequest.builder()
          .budget(null)
          .numberOfPeople(sprayCreateRequest.getNumberOfPeople())
          .build();

      // given
      given(sprayService.createSpray(anyInt(), anyString(), any(SprayCreateRequest.class)))
          .willReturn(sprayCreateResponse);


      RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/spray")
          .headers(httpHeaders)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("utf-8")
          .content(objectMapper.writeValueAsBytes(sprayCreateRequest));

      // when
      ResultActions resultActions = mockMvc.perform(requestBuilder)
          .andDo(print());

      // then
      resultActions
          .andExpect(status().isBadRequest());
    }
  }

}