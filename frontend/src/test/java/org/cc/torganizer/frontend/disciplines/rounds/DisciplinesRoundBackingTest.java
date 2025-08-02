package org.cc.torganizer.frontend.disciplines.rounds;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class DisciplinesRoundBackingTest {

  private DisciplinesRoundBacking backing;

  @BeforeEach
  public void beforeEach() {
    backing = new DisciplinesRoundBacking();
  }

  @Test
  void removeAlreadyAssignedOpponents() {
    var o1 = opponent(1L);
    var o2 = opponent(2L);
    var g1 = group(o1, o2);
    var o3 = opponent(3L);
    var o4 = opponent(4L);
    var g2 = group(o3, o4);
    var r = round(g1, g2);

    var opponents = new ArrayList<Opponent>();
    opponents.add(o1);
    opponents.add(o2);
    opponents.add(o3);
    opponents.add(o4);
    opponents.add(opponent(5L));

    backing.removeAlreadyAssignedOpponents(opponents, r);

    assertThat(opponents).hasSize(1);
    assertThat(opponents.getFirst().getId()).isEqualTo(5L);
  }

  private Group group(Opponent... opponents) {
    var g = new Group();
    for (var o : opponents) {
      g.addOpponent(o);
    }

    return g;
  }

  private Opponent opponent(Long id) {
    var p = new Player(id.toString(), id.toString());
    p.setId(id);

    return p;
  }

  private Round round(Group... groups) {
    var r = new Round();
    for (var g : groups) {
      r.appendGroup(g);
    }

    return r;
  }
}