package org.cc.torganizer.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MathUtilTest {

  @Test
  void testNextSquare_0() {
    int actual = MathUtil.nextSquare(0);
    assertThat(actual).isEqualTo(2);
  }

  @Test
  void testNextSquare_127() {
    int actual = MathUtil.nextSquare(127);
    assertThat(actual).isEqualTo(128);
  }

  @Test
  void testNextSquare_minus1() {
    int actual = MathUtil.nextSquare(-1);
    assertThat(actual).isEqualTo(2);
  }
}