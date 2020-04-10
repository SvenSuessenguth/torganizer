package org.cc.torganizer.core.comparators.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PlayerByLastUpdateComparatorTest {

  PlayerByLastUpdateComparator comparator;

  @BeforeEach
  public void beforeEach() {
    comparator = new PlayerByLastUpdateComparator();
  }

  @ParameterizedTest
  @MethodSource("playerProvider")
  public void test(Player player1, Player player2, int expectedCompare) {
    int actualCompare = comparator.compare(player1, player2);

    assertThat(actualCompare).isEqualTo(expectedCompare);
  }

  static Stream<Arguments> playerProvider() throws ParseException {
    return Stream.of(
        arguments(newPlayerWithLastUpdate("01.01.2020"), newPlayerWithLastUpdate("01.01.2020"), 0),
        arguments(newPlayerWithLastUpdate("01.01.2021"), newPlayerWithLastUpdate("01.01.2020"), -1),
        arguments(newPlayerWithLastUpdate("01.01.2020"), newPlayerWithLastUpdate("01.01.2021"), 1),
        arguments(newPlayerWithLastUpdate(null), newPlayerWithLastUpdate("01.01.2021"), 1),
        arguments(newPlayerWithLastUpdate("01.01.2020"), newPlayerWithLastUpdate(null), -1),
        arguments(newPlayerWithLastUpdate(null), newPlayerWithLastUpdate(null), 0)
    );
  }

  static Player newPlayerWithLastUpdate(String s) throws ParseException {

    Date lastUpdate = null;
    if (s != null) {
      DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
      lastUpdate = formatter.parse(s);
    }

    Player player = new Player("firstName", "lastName");
    player.setLastUpdate(lastUpdate);
    return player;
  }
}