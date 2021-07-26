package org.cc.torganizer.frontend.players.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.cc.torganizer.core.comparators.player.PlayerOrderCriteria.BY_CLUB;

import jakarta.faces.convert.ConverterException;
import java.util.stream.Stream;
import org.cc.torganizer.core.comparators.player.PlayerOrderCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PlayerOrderCriteriaConverterTest {

  private PlayerOrderCriteriaConverter converter;

  @BeforeEach
  public void beforeEach() {
    converter = new PlayerOrderCriteriaConverter();
  }

  public static Stream<Arguments> getAsObject() {
    return Stream.of(
        Arguments.of(null, null),
        Arguments.of("", null),
        Arguments.of(" ", null),
        Arguments.of("BY_CLUB", BY_CLUB),
        Arguments.of(" BY_CLUB ", BY_CLUB)
    );
  }

  @ParameterizedTest
  @MethodSource
  void getAsObject(String value, PlayerOrderCriteria expected) {
    PlayerOrderCriteria actual = converter.getAsObject(null, null, value);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void getAsObject_invalidString() {
    assertThatThrownBy(() -> converter.getAsObject(null, null, "invalid"))
        .isInstanceOf(ConverterException.class);
  }

  public static Stream<Arguments> getAsString() {
    return Stream.of(
        Arguments.of(BY_CLUB, "BY_CLUB"),
        Arguments.of(null, "")
    );
  }

  @ParameterizedTest
  @MethodSource
  void getAsString(PlayerOrderCriteria criteria, String expected) {
    String actual = converter.getAsString(null, null, criteria);
    assertThat(actual).isEqualTo(expected);
  }
}