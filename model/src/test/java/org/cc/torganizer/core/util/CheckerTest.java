package org.cc.torganizer.core.util;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.util.Checker.countNullValues;
import static org.cc.torganizer.core.util.Checker.onlyNullValues;

import java.util.List;
import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CheckerTest {

  @Test
  void countNullValues_nothing() {
    assertThat(countNullValues()).isZero();
  }

  @Test
  void countNullValues_null() {
    assertThat(countNullValues((Object[]) null)).isZero();
  }

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
  void onlyNullValues_empty() {
    assertThat(onlyNullValues()).isTrue();
  }

  @Test
  void onlyNullValues_null() {
    assertThat(onlyNullValues((Object[]) null)).isTrue();
  }

  @Test
  void onlyNullValues_true() {
    assertThat(onlyNullValues(null, null, null)).isTrue();
  }

  @Test
  void onlyNullValues_false() {
    assertThat(onlyNullValues(null, new Object(), null)).isFalse();
  }

  private static final Player PLAYER_ONE = new Player();
  private static final Player PLAYER_TWO = new Player();

  private static Stream<Arguments> equals() {
    return Stream.of(
        Arguments.of(singletonList(new Player()), singletonList(new Player()), false),
        Arguments.of(asList(PLAYER_TWO, PLAYER_ONE), asList(PLAYER_ONE, PLAYER_TWO), true),
        Arguments.of(asList(PLAYER_TWO, PLAYER_ONE), singletonList(PLAYER_ONE), false),
        Arguments.of(emptyList(), asList(PLAYER_ONE, PLAYER_TWO), false),
        Arguments.of(emptyList(), emptyList(), true),
        Arguments.of(null, asList(PLAYER_ONE, PLAYER_TWO), false),
        Arguments.of(asList(PLAYER_ONE, PLAYER_TWO), null, false),
        Arguments.of(null, null, true)
    );
  }

  @ParameterizedTest
  @MethodSource
  void equals(List<?> l1, List<?> l2, boolean expected) {
    boolean equals = Checker.equals(l1, l2);

    assertThat(equals).isEqualTo(expected);
  }

  private static Stream<Arguments> isNoNullValue() {
    return Stream.of(
        Arguments.of(true, new Object[]{}),
        Arguments.of(true, new Object[]{new Object()}),
        Arguments.of(true, null),
        Arguments.of(false, new Object[]{new Object(), null})
    );
  }

  @ParameterizedTest
  @MethodSource
  void isNoNullValue(boolean expected, Object... objects) {
    boolean noNullValue = Checker.isNoNullValue(objects);
    assertThat(noNullValue).isEqualTo(expected);
  }
}