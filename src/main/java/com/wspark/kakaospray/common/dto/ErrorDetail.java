package com.wspark.kakaospray.common.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ErrorDetail implements Serializable {

  private final String field;

  private final Object value;

  private final String message;


}
