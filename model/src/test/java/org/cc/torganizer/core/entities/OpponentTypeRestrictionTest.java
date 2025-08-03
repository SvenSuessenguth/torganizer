package org.cc.torganizer.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.OpponentType.TEAM;

class OpponentTypeRestrictionTest {

  private OpponentTypeRestriction restriction;

  @BeforeEach
  void beforeEach() {
    restriction = new OpponentTypeRestriction();
  }

  @Test
  void isRestricted_null() {
    boolean actual = restriction.isRestricted(null);

    assertThat(actual).isTrue();
  }

  @Test
  void isRestricted_true() {
    restriction.setOpponentType(TEAM);
    var opponent = new Player();
    boolean actual = restriction.isRestricted(opponent);

    assertThat(actual).isTrue();
  }

  @Test
  void isRestricted_false() {
    restriction.setOpponentType(PLAYER);
    var opponent = new Player();
    var actual = restriction.isRestricted(opponent);

    assertThat(actual).isFalse();
  }
}