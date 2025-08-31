package org.cc.torganizer.core.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

class MatchGetWinnerTest {

  private static final String HOME_NAME = "home";
  private static final String GUEST_NAME = "guest";

  @SuppressWarnings("unused")
  private static Stream<Arguments> addMatch() {
    return Stream.of(
      // Gastmannschaft gewinnt
      Arguments.of(matchFactory(0, 1, TRUE), TRUE),
      // Heimmannschaft gewinnt
      Arguments.of(matchFactory(1, 0, TRUE), TRUE),
      // Spiel ist noch nicht beendet
      Arguments.of(matchFactory(1, 0, FALSE), FALSE),
      // Spiel ist unentschieden ausgegangen
      Arguments.of(matchFactory(1, 1, TRUE), FALSE)
    );
  }

  private static Match matchFactory(Integer homeScore, Integer guestScore, boolean finished) {
    var home = new Player(HOME_NAME, HOME_NAME);
    var guest = new Player(GUEST_NAME, GUEST_NAME);
    var match = new Match(home, guest);
    if (finished) {
      match.setFinishedTime(LocalDateTime.now());
    }

    var result = new Result(0, homeScore, guestScore);
    match.addResult(result);

    return match;
  }

  @ParameterizedTest
  @MethodSource("addMatch")
  void testGetWinner(Match match, Boolean matchHasWinner) {
    if (match.isFinished() && matchHasWinner) {
      assertThat(match.getWinner()).isNotNull();
    } else {
      assertThat(match.getWinner()).isInstanceOf(Unknown.class);
    }
  }
}
