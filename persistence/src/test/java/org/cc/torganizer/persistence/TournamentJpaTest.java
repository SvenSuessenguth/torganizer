package org.cc.torganizer.persistence;

import javax.persistence.Query;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class TournamentJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-tournament.xml");
  }

  @Test
  public void testFindAll() {
    Query query = entityManager.createNamedQuery("Tournament.countSubscribers");
    query.setParameter("id", 1L);
    long countSubscribers = (long) query.getSingleResult();
    
    MatcherAssert.assertThat(countSubscribers, Matchers.is(2L));
  }
  
}
