package com.wspark.kakaospray.spray.exception;

import com.wspark.kakaospray.common.exception.wrapper.Unauthorized;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class InvalidRoomException extends Unauthorized {

  public static final String MESSAGE = "잘못된 대화방입니다.";

  public InvalidRoomException() {
    super(MESSAGE);
  }
}
