package com.wspark.kakaospray.util;

import com.wspark.kakaospray.common.util.IntegerUtil;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class IntegerUtilTest {

  @Test
  void divideBy() {
    int n = 100;
    int by = 5;

    int[] divided = IntegerUtil.divideBy(n, by);

    logger.info("divided = " + Arrays.toString(divided));
    for (int i = 0; i < by; i++) {
      n -= divided[i];
    }

    Assertions.assertThat(n).isEqualTo(0);



  }
}