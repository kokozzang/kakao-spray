package com.wspark.kakaospray.spray.exception;

import com.wspark.kakaospray.common.exception.wrapper.Forbidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class SprayStatusExpiredException extends Forbidden {

  public static final String MESSAGE = "조회 기간이 만료된 뿌리기 입니다.";

  public SprayStatusExpiredException() {
    super(MESSAGE);
  }
}
