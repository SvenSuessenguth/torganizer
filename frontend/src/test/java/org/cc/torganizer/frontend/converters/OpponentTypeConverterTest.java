package org.cc.torganizer.frontend.converters;

import jakarta.faces.convert.ConverterException;
import org.cc.torganizer.core.entities.OpponentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.cc.torganizer.core.entities.OpponentType.TEAM;

class OpponentTypeConverterTest {

  private OpponentTypeConverter converter;

  @BeforeEach
  void beforeEach() {
    converter = new OpponentTypeConverter();
  }

  public static Stream<Arguments> getAsObject() {
    return Stream.of(
      Arguments.of(null, null),
      Arguments.of("", null),
      Arguments.of(" ", null),
      Arguments.of("TEAM", TEAM),
      Arguments.of(" TEAM ", TEAM)
    );
  }

  @ParameterizedTest
  @MethodSource
  void getAsObject(String value, OpponentType expected) {
    var actual = converter.getAsObject(null, null, value);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void getAsObject_invalidValue() {
    assertThatThrownBy(() -> converter.getAsObject(null, null, "invalid"))
      .isInstanceOf(ConverterException.class);
  }

  public static Stream<Arguments> getAsString() {
    return Stream.of(
      Arguments.of(null, ""),
      Arguments.of(TEAM, "TEAM")
    );
  }

  @ParameterizedTest
  @MethodSource
  void getAsString(OpponentType value, String expected) {
    var actual = converter.getAsString(null, null, value);
    assertThat(actual).isEqualTo(expected);
  }
}