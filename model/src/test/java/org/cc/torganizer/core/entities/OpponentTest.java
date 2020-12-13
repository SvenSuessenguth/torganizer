package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class OpponentTest {

  @Test
  void testIdleTimePlayer() {
    Player player = createPlayerWithMinutesSinceLastMatch(60);

    Long idleTime = player.getIdleTime();

    assertThat(idleTime).isEqualTo(60);
  }

  @Test
  void testIdleTimeSquad() {
    Player player1 = createPlayerWithMinutesSinceLastMatch(31);
    Player player2 = createPlayerWithMinutesSinceLastMatch(62);
    Squad squad = new Squad();
    squad.addPlayer(player1);
    squad.addPlayer(player2);
    Long idleTime = squad.getIdleTime();

    assertThat(idleTime).isEqualTo(46);
  }

  private Player createPlayerWithMinutesSinceLastMatch(Integer minutesSinceLastMatch) {
    Player player = new Player("firstName", "lastName");
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime lastMatchTime = now.minusMinutes(minutesSinceLastMatch);
    player.setLastMatchTime(lastMatchTime);

    return player;
  }
}