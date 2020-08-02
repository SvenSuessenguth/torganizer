package org.cc.torganizer.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.util.Checker.countNullValues;
import static org.cc.torganizer.core.util.Checker.onlyNullValues;

import org.junit.jupiter.api.Test;

class CheckerTest {

  @Test
  void countNullValues_empty() {
    assertThat(countNullValues()).isZero();
  }

  @Test
  void countNullValues_onlyNullValues() {
    assertThat(countNullValues(null, null)).isEqualTo(2);
  }

  @Test
  void countNullValues_mixedValues() {
    assertThat(countNullValues(null, new Object(), null)).isEqualTo(2);
  }

  @Test
  void countNullValues_onlyNonNullValues() {
    assertThat(countNullValues(new Object(), new Object())).isZero();
  }

  @Test
  void onlyNullValues_null() {
    assertThat(onlyNullValues()).isTrue();
  }

  @Test
  void onlyNullValues_true() {
    assertThat(onlyNullValues(null, null, null)).isTrue();
  }

  @Test
  void onlyNullValues_false() {
    assertThat(onlyNullValues(null, new Object(), null)).isFalse();
  }
}