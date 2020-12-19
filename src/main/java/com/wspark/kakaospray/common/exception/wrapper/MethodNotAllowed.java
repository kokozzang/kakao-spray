package com.wspark.kakaospray.common.exception.wrapper;

import com.wspark.kakaospray.common.enums.ExceptionGroup;
import com.wspark.kakaospray.common.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class MethodNotAllowed extends CommonException {

  public MethodNotAllowed() {
    super(ExceptionGroup.METHOD_NOT_ALLOWED.getCode(), ExceptionGroup.METHOD_NOT_ALLOWED.getMessage());
  }
}
