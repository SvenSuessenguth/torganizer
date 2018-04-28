package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RoundssRepositoryTest extends AbstractDbUnitJpaTest {

  private RoundsRepository repository;

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-round.xml");

    repository = new RoundsRepository(entityManager);
  }

  @Test
  @Ignore("mapping invalid yet")
  public void testReadExisting(){
    Round round = repository.read(1L);
    assertThat(round, is(not(nullValue())));
  }

  @Test
  @Ignore("mapping invalid yet")
  public void testReadNonExisting(){
    Round round = repository.read(100L);
    assertThat(round, is(nullValue()));
  }
}
