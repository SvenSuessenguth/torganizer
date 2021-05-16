package org.cc.torganizer.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.util.Checker.countNullValues;
import static org.cc.torganizer.core.util.Checker.onlyNullValues;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CheckerTest {

  @Test
  void countNullValues_null() {
    assertThat(countNullValues()).isZero();
  }

  @Test
  void countNullValues_empty() {
    assertThat(countNullValues(new Object[]{})).isZero();
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

  private static final Player PLAYER_ONE = new Player();
  private static final Player PLAYER_TWO = new Player();

  private static Stream<Arguments> equals() {
    return Stream.of(
        Arguments.of(Arrays.asList(new Player(), new Player()), Arrays.asList(new Player(), new Player()), false),
        Arguments.of(Arrays.asList(PLAYER_TWO, PLAYER_ONE), Arrays.asList(PLAYER_ONE, PLAYER_TWO), true),
        Arguments.of(Collections.emptyList(), Arrays.asList(PLAYER_ONE, PLAYER_TWO), false),
        Arguments.of(Collections.emptyList(), Collections.emptyList(), true),
        Arguments.of(null, Arrays.asList(PLAYER_ONE, PLAYER_TWO), false),
        Arguments.of(Arrays.asList(PLAYER_ONE, PLAYER_TWO), null, false),
        Arguments.of(null, null, true)
    );
  }

  @ParameterizedTest
  @MethodSource(value = "equals")
  void testEquals(List<?> l1, List<?> l2, boolean expected) {
    boolean equals = Checker.equals(l1, l2);

    assertThat(equals).isEqualTo(expected);
  }
}