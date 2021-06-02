package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.OpponentType.TEAM;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpponentTypeRestrictionTest {

  private OpponentTypeRestriction restriction;

  @BeforeEach
  public void beforeEach() {
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
    Opponent opponent = new Player();
    boolean actual = restriction.isRestricted(opponent);

    assertThat(actual).isTrue();
  }

  @Test
  void isRestricted_false() {
    restriction.setOpponentType(PLAYER);
    Opponent opponent = new Player();
    boolean actual = restriction.isRestricted(opponent);

    assertThat(actual).isFalse();
  }


}