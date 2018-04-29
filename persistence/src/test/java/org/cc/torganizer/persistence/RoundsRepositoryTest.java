package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Round;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class RoundsRepositoryTest extends AbstractDbUnitJpaTest {

  private RoundsRepository repository;

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-round.xml");

    repository = new RoundsRepository(entityManager);
  }

  @Test
  public void testReadExisting(){
    Round round = repository.read(1L);
    assertThat(round, is(not(nullValue())));
  }

  @Test
  public void testReadNonExisting(){
    Round round = repository.read(100L);
    assertThat(round, is(nullValue()));
  }
}