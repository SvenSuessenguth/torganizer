package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.Test;

class MatchTest {

  @Test
  void testIsParticipant_false() {
    Opponent home = new Player();
    Opponent guest = new Player();

    Match match = new Match(home, guest);

    boolean isParticipant = match.isParticipant(new Player());

    assertThat(isParticipant).isFalse();
  }

  @Test
  void testIsParticipant_true() {
    Player home = new Player();
    Player guest = new Player();

    Match match = new Match(home, guest);

    boolean isParticipant = match.isParticipant(home);

    assertThat(isParticipant).isTrue();
  }

  @Test
  void testAddResult_exception() {
    Match match = new Match();
    Throwable throwable = catchThrowable(() -> match.addResult(null));

    assertThat(throwable).isNotNull().isInstanceOf(IllegalArgumentException.class);
  }
}