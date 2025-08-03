package org.cc.torganizer.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.OpponentType.UNKNOWN;

class UnknownTest {

  private Unknown unknown;

  @BeforeEach
  void beforeEach() {
    unknown = new Unknown();
  }

  @Test
  void testGetOpponentType() {
    var opponentType = unknown.getOpponentType();

    assertThat(opponentType).isEqualTo(UNKNOWN);
  }

  @Test
  void testGetPlayers() {
    var players = unknown.getPlayers();

    assertThat(players).isEmpty();
  }
}