package com.wspark.kakaospray.common.exception.wrapper;

import com.wspark.kakaospray.common.enums.ExceptionGroup;
import com.wspark.kakaospray.common.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFound extends CommonException {

  public NotFound() {
    super(ExceptionGroup.NOT_FOUND.getCode(), ExceptionGroup.NOT_FOUND.getMessage());
  }

  public NotFound(String message) {
    super(ExceptionGroup.NOT_FOUND.getCode(), message);
  }
}
