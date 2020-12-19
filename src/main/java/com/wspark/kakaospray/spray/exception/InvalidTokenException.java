package com.wspark.kakaospray.spray.exception;

import com.wspark.kakaospray.common.exception.wrapper.Unauthorized;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends Unauthorized {

  public static final String MESSAGE = "유효하지 않은 토큰입니다.";

  public InvalidTokenException() {
    super(MESSAGE);
  }
}
