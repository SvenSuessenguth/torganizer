package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class GroupsRepositoryTest extends AbstractDbUnitJpaTest {

  private GroupsRepository repository;

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-groups.xml");
    repository = new GroupsRepository(entityManager);
  }

  @Test
  void testRead_null() {
    var g = repository.read(2L);

    assertThat(g).isNull();
  }

  @Test
  void testRead_notNull() {
    var g = repository.read(1L);

    assertThat(g).isNotNull();
  }

  @Test
  void testCount() {
    var count = repository.count();

    assertThat(count).isEqualTo(1L);
  }

  @Test
  void testFilterAlreadyAssignedOpponents_Empty() {
    var round = new Round();
    var opponentsInRound = new HashSet<Opponent>();

    var assignable = repository.filterAlreadyAssignedOpponents(round, opponentsInRound);

    assertThat(assignable).isEmpty();
  }

  @Test
  void testFilterAlreadyAssignedOpponents() {
    // round with one group and two Players
    // player one is already assigned
    var p1 = new Player("a", "b");
    var p2 = new Player("b", "b");

    var round = new Round();
    var group = new Group();
    round.appendGroup(group);
    group.addOpponent(p1);

    var opponentsInRound = new HashSet<Opponent>();
    opponentsInRound.add(p1);
    opponentsInRound.add(p2);

    var assignable = repository.filterAlreadyAssignedOpponents(round, opponentsInRound);

    assertThat(assignable).hasSize(1);

    var o = assignable.iterator().next();
    assertThat(o).isEqualTo(p2);
  }

  @Test
  void testAddOpponent() {
    var group = repository.addOpponent(1L, 3L);

    assertThat(group.getOpponents()).hasSize(3);
  }

  @Test
  void testGetPositionalOpponents() {
    var pOpponents = repository.getPositionalOpponents(1L, 0, 10);
    assertThat(pOpponents).hasSize(2);
  }

  @Test
  void testGetPositionalOpponentsFromNonExistingGroup() {
    var pOpponents = repository.getPositionalOpponents(-1L, 0, 10);
    assertThat(pOpponents).isEmpty();
  }
}
