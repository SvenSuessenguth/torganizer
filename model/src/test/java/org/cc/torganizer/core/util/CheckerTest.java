package org.cc.torganizer.core.util;

import static org.cc.torganizer.core.util.Checker.countNullValues;
import static org.cc.torganizer.core.util.Checker.onlyNullValues;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class CheckerTest {

  @Test
  void countNullValues_empty() {
    assertThat(countNullValues(), is(0));
  }

  @Test
  void countNullValues_onlyNullValues() {
    assertThat(countNullValues(null, null), is(2));
  }

  @Test
  void countNullValues_mixedValues() {
    assertThat(countNullValues(null, new Object(), null), is(2));
  }

  @Test
  void countNullValues_onlyNonNullValues() {
    assertThat(countNullValues(new Object(), new Object()), is(0));
  }

  @Test
  public void onlyNullValues_null() {
    assertThat(onlyNullValues(), is(true));
  }

  @Test
  public void onlyNullValues_true() {
    assertThat(onlyNullValues(null, null, null), is(true));
  }

  @Test
  public void onlyNullValues_false() {
    assertThat(onlyNullValues(null, new Object(), null), is(false));
  }
}