package com.wspark.kakaospray.common.exception.wrapper;

import com.wspark.kakaospray.common.enums.ExceptionGroup;
import com.wspark.kakaospray.common.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends CommonException {

  public InternalServerError() {
    super(ExceptionGroup.INTERNAL_SERVER.getCode(), ExceptionGroup.INTERNAL_SERVER.getMessage());
  }
}
