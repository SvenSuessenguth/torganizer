package org.cc.torganizer.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Result.DEFAULT_POSITION;

class ResultTest {

  private Result result;

  @BeforeEach
  void beforeEach() {
    this.result = new Result();
  }

  @Test
  void testConstructorInitializes() {
    assertThat(result.getPosition()).isEqualTo(DEFAULT_POSITION);
  }

  @Test
  void testParameterizedConstructorInitializes() {
    assertThat(new Result(1).getGuestScore()).isZero();
    assertThat(new Result(1).getHomeScore()).isZero();

    assertThat(new Result(null, 1, 1).getPosition()).isEqualTo(DEFAULT_POSITION);
    assertThat(new Result(1, 1, 1).getPosition()).isEqualTo(1);
  }

  private static Stream<Arguments> areScoresSet() {
    return Stream.of(
      Arguments.of(1, 1, true),
      Arguments.of(null, 1, false),
      Arguments.of(1, null, false),
      Arguments.of(null, null, false)
    );
  }

  @ParameterizedTest
  @MethodSource
  void areScoresSet(Integer homeScore, Integer guestScore, boolean expected) {
    result.setHomeScore(homeScore);
    result.setGuestScore(guestScore);

    var actual = result.areScoresSet();

    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> isDraw() {
    return Stream.of(
      Arguments.of(1, 1, true),
      Arguments.of(1, 2, false),
      Arguments.of(null, 1, false),
      Arguments.of(1, null, false),
      Arguments.of(null, null, false)
    );
  }

  @ParameterizedTest
  @MethodSource
  void isDraw(Integer homeScore, Integer guestScore, boolean expected) {
    result.setHomeScore(homeScore);
    result.setGuestScore(guestScore);

    var actual = result.isDraw();

    assertThat(actual).isEqualTo(expected);
  }
}
