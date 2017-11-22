package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.cc.torganizer.core.entities.Match;
import org.junit.Before;
import org.junit.Test;

public class MatchesJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-matches.xml");
  }

  @Test
  public void testFindById() {
    List<Match> results = entityManager.createNamedQuery("Match.findById", Match.class).setParameter("id", 1L).getResultList();
    
    assertThat(results, hasSize(1));
    Match m = results.get(0);
    
    assertThat(m.getHome().getPlayers().get(0).getPerson().getFirstName(), is("Max"));
    assertThat(m.getGuest().getPlayers().get(0).getPerson().getFirstName(), is("Üöä"));    
  }  
}
