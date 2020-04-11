package org.cc.torganizer.core.comparators.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PlayerByDateOfBirthComparatorTest {

  PlayerByDateOfBirthComparator comparator;

  @BeforeEach
  public void beforeEach() {
    comparator = new PlayerByDateOfBirthComparator();
  }

  @ParameterizedTest
  @MethodSource("playerProvider")
  public void test(Player player1, Player player2, int expectedCompare) {
    int actualCompare = comparator.compare(player1, player2);

    assertThat(actualCompare).isEqualTo(expectedCompare);
  }

  static Stream<Arguments> playerProvider() throws ParseException {
    return Stream.of(
        arguments(newPlayerWithDateOfBirth(null), newPlayerWithDateOfBirth(null), 0),
        arguments(newPlayerWithDateOfBirth("01.01.2020"), newPlayerWithDateOfBirth("01.01.2020"), 0),

        arguments(newPlayerWithDateOfBirth(null), newPlayerWithDateOfBirth("01.01.2020"), -1),
        arguments(newPlayerWithDateOfBirth("01.01.2020"), newPlayerWithDateOfBirth("01.01.2021"), -1),

        arguments(newPlayerWithDateOfBirth("01.01.2020"), newPlayerWithDateOfBirth(null), 1),
        arguments(newPlayerWithDateOfBirth("01.01.2021"), newPlayerWithDateOfBirth("01.01.2020"), 1)
    );
  }

  static Player newPlayerWithDateOfBirth(String s) throws ParseException {
    LocalDate dateOfBirth = null;
    if (s != null) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
      dateOfBirth = LocalDate.parse(s, formatter);
    }

    Player player = new Player("firstName", "lastName");
    player.getPerson().setDateOfBirth(dateOfBirth);

    return player;
  }
}