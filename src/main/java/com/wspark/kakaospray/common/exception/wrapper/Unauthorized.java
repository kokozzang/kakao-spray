package com.wspark.kakaospray.common.exception.wrapper;

import com.wspark.kakaospray.common.enums.ExceptionGroup;
import com.wspark.kakaospray.common.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class Unauthorized extends CommonException {

  public Unauthorized() {
    super(ExceptionGroup.UNAUTHORIZED.getCode(), ExceptionGroup.UNAUTHORIZED.getMessage());
  }

  public Unauthorized(String message) {
    super(ExceptionGroup.UNAUTHORIZED.getCode(), message);
  }

}
