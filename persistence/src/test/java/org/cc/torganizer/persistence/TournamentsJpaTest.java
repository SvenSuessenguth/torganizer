package org.cc.torganizer.persistence;

import java.util.List;
import javax.persistence.Query;

import org.cc.torganizer.core.entities.*;
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
  public void testCountPlayers_existingTournament() {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", 1L);
    long countSubscribers = (long) query.getSingleResult();

    MatcherAssert.assertThat(countSubscribers, Matchers.is(2L));
  }

  @Test
  public void testCountSubscribers_nonExistingTournament() {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", 2L);
    long countSubscribers = (long) query.getSingleResult();

    assertThat(countSubscribers, is(0L));
  }

  @Test
  public void testCountSubscribers_nonExistingTournament_NullId() {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", null);
    long countSubscribers = (long) query.getSingleResult();

    assertThat(countSubscribers, is(0L));
  }

  @Test
  public void testFindPlayers() {
    Query query = entityManager.createNamedQuery("Tournament.findPlayers", Player.class);
    query.setParameter("id", 1L);
    List<Player> players = query.getResultList();

    assertThat(players, hasSize(2));
  }

  @Test
  public void testFindSquads() {
    Query query = entityManager.createNamedQuery("Tournament.findSquads", Squad.class);
    query.setParameter("id", 1L);
    List<Squad> squads = query.getResultList();

    assertThat(squads, hasSize(1));
  }

  @Test
  public void testFindPlayers_none() {
    Query query = entityManager.createNamedQuery("Tournament.findPlayers", Player.class);
    query.setParameter("id", 2L);
    List<Player> players = query.getResultList();

    assertThat(players, hasSize(0));
  }

  @Test
  public void testFindSquads_none() {
    Query query = entityManager.createNamedQuery("Tournament.findSquads", Squad.class);
    query.setParameter("id", 2L);
    List<Squad> squads = query.getResultList();

    assertThat(squads, hasSize(0));
  }

  @Test
  public void testFindDisciplines() {
    Query query = entityManager.createNamedQuery("Tournament.findDisciplines", Discipline.class);
    query.setParameter("id", 1L);
    List<Discipline> disciplines = query.getResultList();

    assertThat(disciplines, hasSize(1));
  }

  @Test
  public void testFindByOpponentType_Player() {
    Query query = entityManager.createNamedQuery("Tournament.findOpponents", Opponent.class);
    query.setParameter("id", 1L);
    List<Opponent> opponents = query.getResultList();

    assertThat(opponents, hasSize(3));
  }
}
