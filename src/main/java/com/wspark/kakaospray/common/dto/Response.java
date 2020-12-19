package com.wspark.kakaospray.common.dto;

import java.io.Serializable;
import lombok.Getter;


@Getter
class Response implements Serializable {

  String code;

  String message;

}
