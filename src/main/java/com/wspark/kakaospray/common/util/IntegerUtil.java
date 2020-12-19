package com.wspark.kakaospray.common.util;

import java.util.Random;

public class IntegerUtil {

  public static int[] divideBy(int n, int by) {
    Random random = new Random();
    int[] divided = new int[by];

    for (int i = 0; i < by - 1; i++) {
      divided[i] = random.nextInt(n);
      n = n - divided[i];
    }
    divided[by-1] = n;

    return divided;
  }

}
