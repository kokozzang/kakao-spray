package com.wspark.kakaospray.spray.exception;

import com.wspark.kakaospray.common.exception.wrapper.Forbidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AlreadyPaidException extends Forbidden {

  public static final String MESSAGE = "지급된 이력이 있습니다.";

  public AlreadyPaidException() {
    super(MESSAGE);
  }
}
