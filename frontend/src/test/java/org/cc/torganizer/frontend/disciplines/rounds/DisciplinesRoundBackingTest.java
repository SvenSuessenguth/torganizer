package org.cc.torganizer.frontend.disciplines.rounds;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisciplinesRoundBackingTest {

  private DisciplinesRoundBacking backing;

  @BeforeEach
  public void beforeEach() {
    backing = new DisciplinesRoundBacking();
  }

  @Test
  void removeAlreadyAssignedOpponents() {
    Opponent o1 = opponent(1L);
    Opponent o2 = opponent(2L);
    Group g1 = group(o1, o2);
    Opponent o3 = opponent(3L);
    Opponent o4 = opponent(4L);
    Group g2 = group(o3, o4);
    Round r = round(g1, g2);

    List<Opponent> opponents = new ArrayList(Arrays.asList(o1,
        o2, o3, o4, opponent(5L)));

    backing.removeAlreadyAssignedOpponents(opponents, r);

    assertThat(opponents).hasSize(1);
    assertThat(opponents.get(0).getId()).isEqualTo(5L);
  }

  private Group group(Opponent... opponents) {
    Group g = new Group();
    for (Opponent o : opponents) {
      g.addOpponent(o);
    }

    return g;
  }

  private Opponent opponent(Long id) {
    Player p = new Player("", "");
    p.setId(id);

    return p;
  }

  private Round round(Group... groups) {
    Round r = new Round();
    for (Group g : groups) {
      r.appendGroup(g);
    }

    return r;
  }
}