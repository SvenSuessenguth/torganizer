package org.cc.torganizer.rest.util;

import static org.cc.torganizer.rest.util.Strings.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class StringsTest {

  @Test
  void isEmpty_null() {
    assertThat(isEmpty(null), is(true));
  }

  @Test
  void isEmpty_empty() {
    assertThat(isEmpty(""), is(true));
  }

  @Test
  void isEmpty_spaces() {
    assertThat(isEmpty("  "), is(true));
  }

  @Test
  void isEmpty_containsSpaces() {
    assertThat(isEmpty("  string  "), is(false));
  }
}