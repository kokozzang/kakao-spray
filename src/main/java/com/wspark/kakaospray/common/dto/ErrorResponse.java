package com.wspark.kakaospray.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wspark.kakaospray.common.exception.CommonException;
import com.wspark.kakaospray.common.exception.custom.BadRequestException;
import java.util.List;
import lombok.Getter;

@Getter
public class ErrorResponse extends Response {

  @JsonInclude(Include.NON_EMPTY)
  private List<ErrorDetail> details;

  public ErrorResponse(CommonException exception) {
    this.code = exception.getCode();
    this.message = exception.getMessage();
  }

  public ErrorResponse(BadRequestException exception) {
    this.code = exception.getCode();
    this.message = exception.getMessage();
    this.details = exception.getDetails();
  }

}
