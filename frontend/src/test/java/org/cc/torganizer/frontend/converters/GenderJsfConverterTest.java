package org.cc.torganizer.frontend.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GenderJsfConverterTest {

  GenderJsfConverter converter;

  static Stream<Arguments> asObject() {
    return Stream.of(
        arguments(null, null),
        arguments("", null),
        arguments(" ", null),
        arguments("ERROR", null),
        arguments("M", MALE)
    );
  }

  static Stream<Arguments> asString() {
    return Stream.of(
        arguments(null, ""),
        arguments(MALE, "M")
    );
  }

  @BeforeEach
  public void beforeEach() {
    converter = new GenderJsfConverter();
  }

  @ParameterizedTest
  @MethodSource("asObject")
  void testGetAsObject(String value, Gender expected) {
    Gender actual = converter.getAsObject(null, null, value);

    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("asString")
  void testGetAsString(Gender gender, String expected) {
    String actual = converter.getAsString(null, null, gender);

    assertThat(actual).isEqualTo(expected);
  }
}