package com.wspark.kakaospray.spray.exception;

import com.wspark.kakaospray.common.exception.wrapper.Forbidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class SprayExpiredException extends Forbidden {

  public static final String MESSAGE = "만료된 뿌리기 입니다.";

  public SprayExpiredException() {
    super(MESSAGE);
  }
}
