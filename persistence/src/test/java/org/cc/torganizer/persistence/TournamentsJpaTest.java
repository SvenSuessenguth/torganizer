package org.cc.torganizer.persistence;

import java.util.List;
import javax.persistence.Query;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;

public class TournamentsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-tournament.xml");
  }

  @Test
  public void testCountSubscribers_existingTournament() {
    Query query = entityManager.createNamedQuery("Tournament.countSubscribers");
    query.setParameter("id", 1L);
    long countSubscribers = (long) query.getSingleResult();
    
    MatcherAssert.assertThat(countSubscribers, Matchers.is(2L));
  }
  
  @Test
  public void testCountSubscribers_nonExistingTournament() {
    Query query = entityManager.createNamedQuery("Tournament.countSubscribers");
    query.setParameter("id", 2L);
    long countSubscribers = (long) query.getSingleResult();
    
    assertThat(countSubscribers, is(0L));
  }
  
  @Test
  public void testCountSubscribers_nonExistingTournament_NullId() {
    Query query = entityManager.createNamedQuery("Tournament.countSubscribers");
    query.setParameter("id", null);
    long countSubscribers = (long) query.getSingleResult();
    
    assertThat(countSubscribers, is(0L));
  }
  
  @Test
  public void testFindPlayers() {
    Query query = entityManager.createNamedQuery("Tournament.findPlayers");
    query.setParameter("id", 1L);
    List<Player> players = query.getResultList();
    
    assertThat(players, hasSize(2));
  }
  
  @Test
  public void testFindSquads() {
    Query query = entityManager.createNamedQuery("Tournament.findSquads");
    query.setParameter("id", 1L);
    List<Squad> squads = query.getResultList();
    
    assertThat(squads, hasSize(1));
  }
  
  @Test
  public void testFindPlayers_none() {
    Query query = entityManager.createNamedQuery("Tournament.findPlayers");
    query.setParameter("id", 2L);
    List<Player> players = query.getResultList();
    
    assertThat(players, hasSize(0));
  }
  
  @Test
  public void testFindSquads_none() {
    Query query = entityManager.createNamedQuery("Tournament.findSquads");
    query.setParameter("id", 2L);
    List<Squad> squads = query.getResultList();
    
    assertThat(squads, hasSize(0));
  }
  
}
