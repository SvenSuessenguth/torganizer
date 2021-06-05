package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupTest {

  private Group group;

  @BeforeEach
  public void beforeEach() {
    group = new Group();
  }

  @Test
  void testSetPositionalOpponents() {
    List<PositionalOpponent> pos = new ArrayList<>();
    pos.add(new PositionalOpponent(new Player("1", "1"), 1));
    pos.add(new PositionalOpponent(new Player("0", "0"), 0));

    assertThat(group.getPositionalOpponents()).isEmpty();

    group.setPositionalOpponents(pos);

    assertThat(group.getPositionalOpponents()).hasSize(2);
    assertThat(group.getPositionalOpponents().get(0).getPosition()).isZero();
  }

  @Test
  void testRemoveOpponent_null() {
    List<Opponent> opponentsBefore = group.getOpponents();
    group.removeOpponent(null);
    List<Opponent> opponentsAfter = group.getOpponents();

    assertThat(opponentsAfter).containsOnlyOnceElementsOf(opponentsBefore);
  }

  @Test
  void testRemoveOpponent_reindex() {
    Player playerToRemove = new Player();
    Player testPlayer = new Player();

    group.addOpponent(new Player());
    group.addOpponent(playerToRemove);
    group.addOpponent(testPlayer);

    int indexBefore = group.getPositionalOpponent(testPlayer).getPosition();
    group.removeOpponent(playerToRemove);
    int indexAfter = group.getPositionalOpponent(testPlayer).getPosition();
    List<Opponent> opponentsAfter = group.getOpponents();

    assertThat(opponentsAfter).hasSize(2);
    assertThat(indexBefore).isNotEqualTo(indexAfter);
  }

  @Test
  void testGetPositionalOpponent_null() {
    PositionalOpponent positionalOpponent = group.getPositionalOpponent((Opponent) null);

    assertThat(positionalOpponent).isNull();
  }
}