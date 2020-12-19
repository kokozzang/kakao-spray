package com.wspark.kakaospray.common.exception.custom;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.wspark.kakaospray.common.dto.ErrorDetail;
import com.wspark.kakaospray.common.dto.ErrorDetail.ErrorDetailBuilder;
import com.wspark.kakaospray.common.exception.wrapper.BadRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Slf4j
public class BadRequestException extends BadRequest {

  @Getter
  private List<ErrorDetail> details = Lists.newArrayList();

  private BadRequestException() {}


  public BadRequestException(List<ErrorDetail> errorDetails) {
    this.details = errorDetails;
  }

  public BadRequestException(Exception exception) throws Exception {

    if (exception instanceof BadRequestException) {
      this.handleBadRequest((BadRequestException) exception);
    }
    else if (exception instanceof MethodArgumentNotValidException) {
      this.handleBadRequest((MethodArgumentNotValidException) exception);
    }
    else if (exception instanceof MethodArgumentTypeMismatchException) {
      this.handleBadRequest((MethodArgumentTypeMismatchException) exception);
    }
    else if (exception instanceof MissingServletRequestParameterException) {
      this.handleBadRequest((MissingServletRequestParameterException) exception);
    }
    else if (exception instanceof HttpMessageNotReadableException) {
      this.handleBadRequest((HttpMessageNotReadableException) exception);
    }
    else if (exception instanceof MissingRequestHeaderException) {
      this.handleBadRequest((MissingRequestHeaderException) exception);
    }
    else if (exception instanceof BindException) {
      this.handleBadRequest((BindException) exception);
    }
    else {
      throw exception;
    }

  }

  /**
   * 400 Bad Request
   * 커스텀 밸리데이션 에러 핸들링
   * https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400
   * https://tools.ietf.org/html/rfc7231#section-6.5.4
   * @param exception
   */
  private void handleBadRequest(BadRequestException exception) {
    this.details = exception.getDetails();
  }

  /**
   * bean validation
   * @param exception bean validation으로 발생한 exception 객체
   * @param exception
   */
  private void handleBadRequest(BindException exception) {
    this.details = this.getErrorDetailsByBindingResult(exception.getBindingResult());
  }

  /**
   * bean validation
   *  @param exception bean validation으로 발생한 exception 객체
   */
  private void handleBadRequest(MethodArgumentNotValidException exception) {
    this.details = this.getErrorDetailsByBindingResult(exception.getBindingResult());
  }

  private List<ErrorDetail> getErrorDetailsByBindingResult(BindingResult bindingResult) {
    List<ErrorDetail> errorDetails = Lists.newArrayList();

    bindingResult.getAllErrors().forEach(error -> {
      FieldError fieldError = (FieldError) error;
      String field = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldError.getField());

      ErrorDetailBuilder errorDetailBuilder = ErrorDetail.builder()
          .field(field)
          .value(fieldError.getRejectedValue())
          .message(fieldError.getDefaultMessage());

      if (fieldError.contains(TypeMismatchException.class)) {
        errorDetailBuilder.message("Type mismatch.");
      }

      ErrorDetail errorDetail = errorDetailBuilder.build();

      errorDetails.add(errorDetail);
    });

    return errorDetails;
  }

  /**
   * query string parameter 타입 오류
   * @param exception query string 파라미터 바인딩시 발생한 exception 객체
   */
  private void handleBadRequest(MethodArgumentTypeMismatchException exception) {
    this.message = "Type mismatch for \'" + exception.getName() + "\'";
  }

  /**
   * 필수 query string parameter 누락
   * @param exception query string 파라미터 바인딩시 발생한 exception 객체
   */
  private void handleBadRequest(MissingServletRequestParameterException exception) {
    this.message = "Required parameter \'" + exception.getParameterName() + "\' is not present";
  }

  /**
   * header missing
   * @param exception 헤더 없음
   * @param exception
   */
  private void handleBadRequest(MissingRequestHeaderException exception) {
    this.message = exception.getMessage();
  }

  /**
   * json 파싱 에러
   * @param exception
   */
  private void handleBadRequest(HttpMessageNotReadableException exception) {

    if (exception.getCause() instanceof InvalidFormatException) {
      InvalidFormatException invalidFormatException = (InvalidFormatException) exception.getCause();
      String field = invalidFormatException.getPath().get(0).getFieldName();
      Object value = invalidFormatException.getValue();
      String message = "Invalid format.";

      // Enum binding 실패
      if (Enum.class.isAssignableFrom(invalidFormatException.getTargetType())) {
        Object[] enumConstants = invalidFormatException.getTargetType().getEnumConstants();

        StringBuilder messageStringBuilder = new StringBuilder("Not one of the values accepted for [");
        String enumNames = Arrays.stream((Enum<?>[]) enumConstants).map(Enum::name).collect(Collectors.joining(", "));
        messageStringBuilder.append(enumNames);
        messageStringBuilder.append("]");

        message = messageStringBuilder.toString();

        ErrorDetail errorDetail = ErrorDetail.builder()
            .field(field)
            .value(value)
            .message(messageStringBuilder.toString())
            .build();

        this.details = Lists.newArrayList(errorDetail);
      } else {
        logger.error(exception.getMessage(), exception);
      }

      ErrorDetail errorDetail = ErrorDetail.builder()
          .field(field)
          .value(value)
          .message(message)
          .build();

      this.details = Lists.newArrayList(errorDetail);

    } else {
      this.message = "JSON parse error";
      logger.error(exception.getMessage(), exception);
    }
  }


}
