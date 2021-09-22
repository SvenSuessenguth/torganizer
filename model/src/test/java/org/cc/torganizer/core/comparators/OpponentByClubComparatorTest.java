package org.cc.torganizer.core.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OpponentByClubComparatorTest {

  private OpponentsByClubComparator comparator;

  @BeforeEach
  public void before() {
    comparator = new OpponentsByClubComparator();
  }

  public static Stream<Arguments> compare() {
    Club club1 = new Club("club1");
    Club club2 = new Club("club2");

    return Stream.of(
        Arguments.of(new Player(), new Player(), 0),
        Arguments.of(null, null, 0),
        Arguments.of(new Player(), null, -1),
        Arguments.of(null, new Player(), 1),
        Arguments.of(pwc(club1), pwc(club1), 0),
        Arguments.of(pwc(club2), pwc(club1), 1),
        Arguments.of(pwc(club1), pwc(club2), -1),
        Arguments.of(swp(pwc(club1), pwc(club2)), swp(pwc(club2), pwc(club1)), 0),
        Arguments.of(swp(pwc(club1), pwc(club2)), swp(pwc(club2), new Player()), -1)
    );
  }

  // pwc = player with club
  private static Player pwc(Club club) {
    Player player = new Player();
    player.setClub(club);
    return player;
  }

  // swp = squad with players
  private static Squad swp(Player... players) {
    Squad squad = new Squad();
    for (Player player : players) {
      squad.addPlayer(player);
    }
    return squad;
  }

  @ParameterizedTest
  @MethodSource
  void compare(Opponent o1, Opponent o2, int expected) {
    assertThat(comparator.compare(o1, o2)).isEqualTo(expected);
  }
}
