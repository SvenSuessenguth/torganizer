package org.cc.torganizer.rest.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.rest.util.Strings.isEmpty;

import org.junit.jupiter.api.Test;

class StringsTest {

  @Test
  void isEmpty_null() {
    assertThat(isEmpty(null)).isTrue();
  }

  @Test
  void isEmpty_empty() {
    assertThat(isEmpty("")).isTrue();
  }

  @Test
  void isEmpty_spaces() {
    assertThat(isEmpty("  ")).isTrue();
  }

  @Test
  void isEmpty_containsSpaces() {
    assertThat(isEmpty("  string  ")).isFalse();
  }
}