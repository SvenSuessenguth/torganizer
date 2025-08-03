package org.cc.torganizer.core.comparators.player;

import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.comparators.player.PlayerOrderCriteria.BY_DATE_OF_BIRTH;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PlayerByDateOfBirthComparatorTest {

  PlayerByDateOfBirthComparator comparator;

  @BeforeEach
  void beforeEach() {
    comparator = new PlayerByDateOfBirthComparator();
  }

  @Test
  void getPlayerOrderCriteria() {
    assertThat(comparator.getPlayerOrderCriteria()).isEqualTo(BY_DATE_OF_BIRTH);
  }

  public static Stream<Arguments> comparePlayers() {
    return Stream.of(
      arguments(null, null, 0),
      arguments(null, newPlayerWithDateOfBirth(null), -1),
      arguments(newPlayerWithDateOfBirth(null), null, 1),
      arguments(newPlayerWithDateOfBirth(null), newPlayerWithDateOfBirth(null), 0),
      arguments(newPlayerWithDateOfBirth("01.01.2020"), newPlayerWithDateOfBirth("01.01.2020"), 0),

      arguments(newPlayerWithDateOfBirth(null), newPlayerWithDateOfBirth("01.01.2020"), -1),
      arguments(newPlayerWithDateOfBirth("01.01.2020"), newPlayerWithDateOfBirth("01.01.2021"), -1),

      arguments(newPlayerWithDateOfBirth("01.01.2020"), newPlayerWithDateOfBirth(null), 1),
      arguments(newPlayerWithDateOfBirth("01.01.2021"), newPlayerWithDateOfBirth("01.01.2020"), 1)
    );
  }

  @ParameterizedTest
  @MethodSource("comparePlayers")
  void compare(Player player1, Player player2, int expectedCompare) {
    var actualCompare = comparator.compare(player1, player2);

    assertThat(actualCompare).isEqualTo(expectedCompare);
  }

  static Player newPlayerWithDateOfBirth(String s) {
    LocalDate dateOfBirth = null;
    if (s != null) {
      var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
      dateOfBirth = LocalDate.parse(s, formatter);
    }

    var player = new Player("firstName", "lastName");
    player.getPerson().setDateOfBirth(dateOfBirth);

    return player;
  }
}