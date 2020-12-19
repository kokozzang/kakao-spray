package com.wspark.kakaospray.spray.exception;

import com.wspark.kakaospray.common.exception.wrapper.Forbidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AccessByOwnerException extends Forbidden {

  public static final String MESSAGE = "자신의 뿌리기를 받을 수 없습니다.";

  public AccessByOwnerException() {
    super(MESSAGE);
  }
}
