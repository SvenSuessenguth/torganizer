package org.cc.torganizer.core.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class MatchTest {

  @Test
  void testIsParticipant_false() {
    var home = new Player("1", "1");
    var guest = new Player("2", "2");

    var match = new Match(home, guest);

    var isParticipant = match.isParticipant(new Player("3", "3"));

    assertThat(isParticipant).isFalse();
  }

  @Test
  void testIsParticipant_true() {
    var home = new Player();
    var guest = new Player();

    var match = new Match(home, guest);

    var isParticipant = match.isParticipant(home);

    assertThat(isParticipant).isTrue();
  }

  @Test
  void testAddResult_exception() {
    var match = new Match();
    var throwable = catchThrowable(() -> match.addResult(null));

    assertThat(throwable).isNotNull().isInstanceOf(IllegalArgumentException.class);
  }
}