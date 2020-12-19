package com.wspark.kakaospray.common.exception.wrapper;

import com.wspark.kakaospray.common.enums.ExceptionGroup;
import com.wspark.kakaospray.common.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequest extends CommonException {

  public BadRequest() {
    super(ExceptionGroup.BAD_REQUEST.getCode(), ExceptionGroup.BAD_REQUEST.getMessage());
  }

  public BadRequest(String message) {
    super(ExceptionGroup.BAD_REQUEST.getCode(), message);
  }

}
