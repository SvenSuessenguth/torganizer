package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Opponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DisciplinesRepositoryOpponentsAssignableToRoundTest extends AbstractDbUnitJpaTest {

  private DisciplinesRepository repository;

  @BeforeEach
  public void before() throws Exception {
    super.initDatabase("test-data-discipline.xml");

    repository = new DisciplinesRepository(entityManager);
  }

  @Test
  public void testGetOpponentsAssignableToRound(){
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    List<Opponent> opponentsAssignableToRound = repository.getOpponentsAssignableToRound(1L);

    for(Opponent o  : opponentsAssignableToRound){
      System.out.println(o);
    }

    assertThat(opponentsAssignableToRound, is(not(empty())));
  }
}
