package org.cc.torganizer.persistence;

import java.util.List;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Player;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
    
    Player home = m.getHome().getPlayers().iterator().next();
    Player guest = m.getGuest().getPlayers().iterator().next();
    
    assertThat(home.getPerson().getFirstName(), is("Max"));
    assertThat(guest.getPerson().getFirstName(), is("Üöä"));    
  }  
}
