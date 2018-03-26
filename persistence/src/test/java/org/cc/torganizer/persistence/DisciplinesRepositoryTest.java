package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.cc.torganizer.core.entities.Status.INACTIVE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DisciplinesRepositoryTest extends AbstractDbUnitJpaTest {

  private DisciplinesRepository repository;

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-tournament.xml");

    repository = new DisciplinesRepository(entityManager);
  }

  @Test
  public void testGetOpponents() {
    List<Opponent> opponents = repository.getOpponents(1L);

    assertThat(opponents, is(not(nullValue())));
    assertThat(opponents, hasSize(2));
  }
}
