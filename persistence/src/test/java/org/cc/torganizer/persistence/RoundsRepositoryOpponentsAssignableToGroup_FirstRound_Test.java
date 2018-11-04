package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Opponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RoundsRepositoryOpponentsAssignableToGroup_FirstRound_Test extends AbstractDbUnitJpaTest {

  private RoundsRepository repository;

  @BeforeEach
  public void before() throws Exception {
    super.initDatabase("test-data-round_group.xml");
    repository = new RoundsRepository(entityManager, new DisciplinesRepository(entityManager));
  }

  @Test
  public void testGetOpponentsAssignableToRound(){
    Set<Opponent> opponentsAssignableToRound = repository.getNotAssignedOpponents(1L);

    assertThat(opponentsAssignableToRound, is(not(empty())));
    assertThat(opponentsAssignableToRound, hasSize(1));
  }

  @Test
  public void testGetAlreadyAssignedOpponents(){
    Set<Opponent> alreadyAssignedOpponents = repository.getAssignedOpponents(1L);

    assertThat(alreadyAssignedOpponents, hasSize(2));
  }
}
