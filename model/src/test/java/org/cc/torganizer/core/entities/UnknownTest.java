package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.OpponentType.UNKNOWN;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnknownTest {

  private Unknown unknown;

  @BeforeEach
  public void beforeEach() {
    unknown = new Unknown();
  }

  @Test
  void testGetOpponentType() {
    OpponentType opponentType = unknown.getOpponentType();

    assertThat(opponentType).isEqualTo(UNKNOWN);
  }

  @Test
  void testGetPlayers() {
    Set<Player> players = unknown.getPlayers();

    assertThat(players).isEmpty();
  }
}