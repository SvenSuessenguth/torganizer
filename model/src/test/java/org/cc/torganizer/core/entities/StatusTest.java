package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StatusTest {

  public static Stream<Arguments> fromInt() {
    return Stream.of(
        Arguments.of(0, Status.INACTIVE),
        Arguments.of(1, Status.ACTIVE),
        Arguments.of(2, null),
        Arguments.of(-1, null)
    );
  }

  @ParameterizedTest
  @MethodSource("fromInt")
  void fromInt(int value, Status expected) {
    Status actual = Status.fromInt(value);

    assertThat(actual).isEqualTo(expected);
  }
}