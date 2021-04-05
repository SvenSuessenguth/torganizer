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
    Group g1 = group(opponent(1L), opponent(2L));
    Group g2 = group(opponent(3L), opponent(4L));
    Round r = round(g1, g2);

    List<Opponent> opponents = new ArrayList<>(Arrays.asList(opponent(1L),
        opponent(2L), opponent(3L), opponent(4L), opponent(5L)));

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