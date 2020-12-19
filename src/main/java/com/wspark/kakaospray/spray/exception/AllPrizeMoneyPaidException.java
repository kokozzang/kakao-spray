package com.wspark.kakaospray.spray.exception;

import com.wspark.kakaospray.common.exception.wrapper.Forbidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AllPrizeMoneyPaidException extends Forbidden {

  public static final String MESSAGE = "뿌리기가 모두 지급되었습니다.";

  public AllPrizeMoneyPaidException() {
    super(MESSAGE);
  }
}
