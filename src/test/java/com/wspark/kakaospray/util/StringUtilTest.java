package com.wspark.kakaospray.util;

import com.wspark.kakaospray.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class StringUtilTest {


  @Test
  void generateToken() {

    for (int i = 0; i < 10; i++) {
      logger.info(StringUtil.generateToken());
    }
  }

}