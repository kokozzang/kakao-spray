package com.wspark.kakaospray.common.exception.wrapper;

import com.wspark.kakaospray.common.enums.ExceptionGroup;
import com.wspark.kakaospray.common.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class Forbidden extends CommonException {

  public Forbidden() {
    super(ExceptionGroup.FORBIDDEN.getCode(), ExceptionGroup.FORBIDDEN.getMessage());
  }

  public Forbidden(String message) {
    super(ExceptionGroup.FORBIDDEN.getCode(), message);
  }

}
