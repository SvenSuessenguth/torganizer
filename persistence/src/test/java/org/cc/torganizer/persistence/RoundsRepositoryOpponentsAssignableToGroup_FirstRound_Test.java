package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.cc.torganizer.core.entities.Opponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoundsRepositoryOpponentsAssignableToGroup_FirstRound_Test extends AbstractDbUnitJpaTest {

  private RoundsRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-round_group.xml");
    repository = new RoundsRepository(entityManager, new DisciplinesRepository(entityManager));
  }

  @Test
  void testGetOpponentsAssignableToRound(){
    Set<Opponent> opponentsAssignableToRound = repository.getNotAssignedOpponents(1L);

    assertThat(opponentsAssignableToRound).isNotEmpty();
    assertThat(opponentsAssignableToRound).hasSize(1);
  }

  @Test
  void testGetAlreadyAssignedOpponents(){
    Set<Opponent> alreadyAssignedOpponents = repository.getAssignedOpponents(1L);

    assertThat(alreadyAssignedOpponents).hasSize(2);
  }
}
