package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
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

  @Test
  void testSetOpponents_null() {
    group.setOpponents(null);
    List<PositionalOpponent> positionalOpponents = group.getPositionalOpponents();
    assertThat(positionalOpponents).isEmpty();
  }

  @Test
  void testSetOpponents_empty() {
    group.setOpponents(new ArrayList<>());
    List<PositionalOpponent> positionalOpponents = group.getPositionalOpponents();
    assertThat(positionalOpponents).isEmpty();
  }

  @Test
  void testSetOpponents() {
    group.setOpponents(Arrays.asList(new Player(), new Player()));
    List<PositionalOpponent> positionalOpponents = group.getPositionalOpponents();
    assertThat(positionalOpponents).hasSize(2);
  }

  @Test
  void testGetOpponent_null() {
    Opponent opponent = group.getOpponent(null);
    assertThat(opponent).isNull();
  }

  @Test
  void testGetOpponent_invalid() {
    Opponent opponent = group.getOpponent(Integer.MIN_VALUE);
    assertThat(opponent).isNull();
  }

  @Test
  void testGetOpponent_valid() {
    Player expected = new Player();
    group.addOpponent(expected);
    Opponent opponent = group.getOpponent(0);
    assertThat(opponent).isNotNull().isEqualTo(expected);
  }
}