package org.cc.torganizer.frontend.converters;

import jakarta.faces.convert.ConverterException;
import org.cc.torganizer.core.entities.System;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SystemConverterTest {

  private SystemConverter converter;

  @BeforeEach
  void beforeEach() {
    converter = new SystemConverter();
  }

  public static Stream<Arguments> getAsObject() {
    return Stream.of(
      Arguments.of("DOUBLE_ELIMINATION", System.DOUBLE_ELIMINATION),
      Arguments.of(null, null),
      Arguments.of("", null)
    );
  }

  @ParameterizedTest
  @MethodSource
  void getAsObject(String string, System expected) {
    var actual = converter.getAsObject(null, null, string);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void getAsObject_invalid() {
    assertThatThrownBy(() -> converter.getAsObject(null, null, "invalid"))
      .isInstanceOf(ConverterException.class);
  }

  public static Stream<Arguments> getAsString() {
    return Stream.of(
      Arguments.of(System.DOUBLE_ELIMINATION, "DOUBLE_ELIMINATION"),
      Arguments.of(null, "")
    );
  }

  @ParameterizedTest
  @MethodSource
  void getAsString(System system, String expected) {
    var actual = converter.getAsString(null, null, system);
    assertThat(actual).isEqualTo(expected);
  }
}