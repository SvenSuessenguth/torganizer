package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GroupsRepositoryTest extends AbstractDbUnitJpaTest {

  private GroupsRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-groups.xml");
    repository = new GroupsRepository(entityManager);
  }

  @Test
  void testRead_null(){
    Group g = repository.read(2L);

    assertThat(g).isNull();
  }

  @Test
  void testRead_notNull(){
    Group g = repository.read(1L);

    assertThat(g).isNotNull();
  }

  @Test
  void testCount(){
    long count = repository.count();

    assertThat(count).isEqualTo(1L);
  }

  @Test
  void testFilterAlreadyAssignedOpponents_Empty(){
    Round round = new Round();
    Set<Opponent> opponentsInRound = new HashSet<>();

    Set<Opponent> assignable = repository.filterAlreadyAssignedOpponents(round, opponentsInRound);

    assertThat(assignable).isEmpty();
  }

  @Test
  void testFilterAlreadyAssignedOpponents(){
    // round with one group and two Players
    // player one is already assigned
    Player p1 = new Player("a", "b");
    Player p2 = new Player("b", "b");

    Round round = new Round();
    Group group = new Group();
    round.appendGroup(group);
    group.addOpponent(p1);

    Set<Opponent> opponentsInRound = new HashSet<>();
    opponentsInRound.add(p1);
    opponentsInRound.add(p2);

    Set<Opponent> assignable = repository.filterAlreadyAssignedOpponents(round, opponentsInRound);

    assertThat(assignable).hasSize(1);

    Opponent o = assignable.iterator().next();
    assertThat(o).isEqualTo(p2);
  }

  @Test
  void testAddOpponent(){
    Group group = repository.addOpponent(1L, 3L);

    assertThat(group.getOpponents()).hasSize(3);
  }

  @Test
  void testGetPositionalOpponents(){
    List<PositionalOpponent> pOpponents = repository.getPositionalOpponents(1L, 0, 10);
    assertThat(pOpponents).hasSize(2);
  }

  @Test
  void testGetPositionalOpponentsFromNonExistingGroup(){
    List<PositionalOpponent> pOpponents = repository.getPositionalOpponents(-1L, 0, 10);
    assertThat(pOpponents).hasSize(0);
  }
}
