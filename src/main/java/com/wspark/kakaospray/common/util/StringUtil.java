package com.wspark.kakaospray.common.util;


import org.apache.commons.lang3.RandomStringUtils;

public class StringUtil {

  public static String generateToken() {
    return RandomStringUtils.randomAlphanumeric(3);
  }

}
