package org.cc.torganizer.core.entities;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.Gender.UNKNOWN;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.text.ParseException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GenderRestrictionTest {

  static Stream<Arguments> playerProvider() throws ParseException {
    return Stream.of(
        arguments(new GenderRestriction(MALE), newPlayerWithGender(MALE), FALSE),
        arguments(new GenderRestriction(FEMALE), newPlayerWithGender(MALE), TRUE),
        arguments(new GenderRestriction(MALE), newPlayerWithGender(null), FALSE),
        arguments(new GenderRestriction(MALE), newPlayerWithGender(UNKNOWN), FALSE)
    );
  }

  static Player newPlayerWithGender(Gender gender) {
    Player player = new Player("firstName", "lastName");
    player.getPerson().setGender(gender);
    return player;
  }

  @ParameterizedTest
  @MethodSource("playerProvider")
  void testIsGenderRestricted(GenderRestriction restriction, Player player, Boolean expected) {
    boolean actual = restriction.isGenderRestricted(player);

    assertThat(actual).isEqualTo(expected);
  }
}