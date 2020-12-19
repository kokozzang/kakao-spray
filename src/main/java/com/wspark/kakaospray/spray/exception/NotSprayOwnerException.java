package com.wspark.kakaospray.spray.exception;

import com.wspark.kakaospray.common.exception.wrapper.Forbidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class NotSprayOwnerException extends Forbidden {

  public static final String MESSAGE = "뿌리기 소유자가 아닙니다.";

  public NotSprayOwnerException() {
    super(MESSAGE);
  }
}
