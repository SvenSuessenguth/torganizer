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
    assertThat(group.getPositionalOpponents().get(0).getPosition()).isEqualTo(0);
  }
}