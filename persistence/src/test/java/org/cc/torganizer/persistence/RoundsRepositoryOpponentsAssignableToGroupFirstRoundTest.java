package org.cc.torganizer.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoundsRepositoryOpponentsAssignableToGroupFirstRoundTest extends AbstractDbUnitJpaTest {

  private RoundsRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-round_group.xml");
    repository = new RoundsRepository(entityManager, new DisciplinesRepository(entityManager));
  }

  @Test
  void testGetOpponentsAssignableToRound() {
    var opponentsAssignableToRound = repository.getNotAssignedOpponents(1L, 0, 10);

    assertThat(opponentsAssignableToRound).hasSize(1);
  }

  @Test
  void testGetOpponentsAssignableToRound_offsetMaxResults() {
    var opponentsAssignableToRound = repository.getNotAssignedOpponents(1L, 0, 0);

    assertThat(opponentsAssignableToRound).isEmpty();
  }

  @Test
  void testGetAlreadyAssignedOpponents() {
    var alreadyAssignedOpponents = repository.getAssignedOpponents(1L);

    assertThat(alreadyAssignedOpponents).hasSize(2);
  }
}
