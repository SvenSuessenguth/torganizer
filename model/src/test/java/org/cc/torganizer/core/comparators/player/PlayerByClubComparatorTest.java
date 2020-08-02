package org.cc.torganizer.core.comparators.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


class PlayerByClubComparatorTest {

  PlayerByClubComparator comparator;

  static Stream<Arguments> playerProvider() {
    return Stream.of(
        arguments(newPlayerWithClub("a"), newPlayerWithClub("b"), -1),
        arguments(newPlayerWithClub("b"), newPlayerWithClub("a"), 1),
        arguments(newPlayerWithClub("a"), newPlayerWithClub("a"), 0),
        arguments(newPlayerWithClub(""), newPlayerWithClub(""), 0),
        arguments(newPlayerWithClub(null), newPlayerWithClub(null), 0),
        arguments(newPlayerWithClub("a"), newPlayerWithClub(null), -1),
        arguments(newPlayerWithClub(null), newPlayerWithClub("a"), 1)
    );
  }

  static Player newPlayerWithClub(String clubName) {
    Player player = new Player("firstName", "lastName");
    Club club = new Club(clubName);
    player.setClub(club);

    return player;
  }

  @BeforeEach
  public void beforeEach() {
    comparator = new PlayerByClubComparator();
  }

  @ParameterizedTest
  @MethodSource("playerProvider")
  void test(Player player1, Player player2, int expectedCompare) {
    int actualCompare = comparator.compare(player1, player2);

    assertThat(actualCompare).isEqualTo(expectedCompare);
  }
}