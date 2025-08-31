package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class OpponentTest {

  @Test
  void testIdleTimePlayer() {
    var player = createPlayerWithMinutesSinceLastMatch(60);

    var idleTime = player.getIdleTime();

    assertThat(idleTime).isEqualTo(60);
  }

  @Test
  void testIdleTimeSquad() {
    var player1 = createPlayerWithMinutesSinceLastMatch(31);
    var player2 = createPlayerWithMinutesSinceLastMatch(62);
    var squad = new Squad();
    squad.addPlayer(player1);
    squad.addPlayer(player2);
    var idleTime = squad.getIdleTime();

    assertThat(idleTime).isEqualTo(46);
  }

  private Player createPlayerWithMinutesSinceLastMatch(Integer minutesSinceLastMatch) {
    var player = new Player("firstName", "lastName");
    var now = LocalDateTime.now();
    var lastMatchTime = now.minusMinutes(minutesSinceLastMatch);
    player.setLastMatchTime(lastMatchTime);

    return player;
  }
}