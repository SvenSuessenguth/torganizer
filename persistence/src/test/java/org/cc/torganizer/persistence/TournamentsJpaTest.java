package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.*;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TournamentsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-tournament.xml");
  }

  @Test
  public void testCriteriaCountOpponents(){

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> cq = cb.createTupleQuery();
    Root<Tournament> tournament = cq.from(Tournament.class);
    Join<Tournament, Opponent> tournamentOpponentJoin = tournament.join("opponents", JoinType.LEFT);
    cq.select(cb.tuple(tournament, cb.count(tournamentOpponentJoin)));
    cq.where(cb.equal(tournament.get("id"), 1L));

    List<Tuple> result = entityManager.createQuery(cq).getResultList();
    Long count = (Long) result.get(0).get(1);

    // 2 Player + 1 Squad
    assertThat(count, is(3L));
  }

  @Test
  public void testCriteriaCountPlayers(){

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> cq = cb.createTupleQuery();
    Root<Tournament> tournament = cq.from(Tournament.class);
    Join<Tournament, Opponent> tournamentOpponentJoin = tournament.join("opponents", JoinType.LEFT);
    cq.select(cb.tuple(tournament, cb.count(tournamentOpponentJoin)));
    cq.where(
      cb.and(
        cb.equal(tournament.get("id"), 1L),
        cb.equal(tournamentOpponentJoin.type(), Player.class)
      )
    );

    List<Tuple> result = entityManager.createQuery(cq).getResultList();
    Long count = (Long) result.get(0).get(1);

    assertThat(count, is(2L));
  }

  @Test
  public void testCriteriaCountSquads(){
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> cq = cb.createTupleQuery();
    Root<Tournament> tournament = cq.from(Tournament.class);
    Join<Tournament, Opponent> tournamentOpponentJoin = tournament.join("opponents", JoinType.LEFT);
    cq.select(cb.tuple(tournament, cb.count(tournamentOpponentJoin)));
    cq.where(
      cb.and(
        cb.equal(tournament.get("id"), 1L),
        cb.equal(tournamentOpponentJoin.type(), Squad.class)
      )
    );

    List<Tuple> result = entityManager.createQuery(cq).getResultList();
    Long count = (Long) result.get(0).get(1);

    assertThat(count, is(1L));
  }

  @Test
  public void testCriteriaListPlayersOrderedByLastName(){
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Player> cq = cb.createQuery(Player.class);
    Root<Tournament> tournament = cq.from(Tournament.class);
    Root<Player> player = cq.from(Player.class);
    Join<Tournament, Player> tournamentOpponentJoin = tournament.join ("opponents");

    cq.select(player);
    cq.where(
      cb.and(
        cb.equal(tournament.get("id"), 1L),
        cb.equal(tournamentOpponentJoin.type(), Player.class),
        cb.equal(player.get("id"), tournamentOpponentJoin.get("id"))
      )
    );
    cq.orderBy(
      cb.asc(
        player.get("person").get("lastName")
      )
    );

    TypedQuery<Player> query = entityManager.createQuery(cq);
    query.setFirstResult(0);
    query.setMaxResults(5);
    List<Player> result = query.getResultList();

    assertThat(result.get(0).getPerson().getLastName(), is("Aöüß"));
  }

  @Test
  public void testCountPlayers_existingTournament() {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", 1L);
    long countSubscribers = (long) query.getSingleResult();

    assertThat(countSubscribers, Matchers.is(2L));
  }

  @Test
  public void testCountSubscribers_nonExistingTournament() {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", 3L);
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
    query.setParameter("id", 3L);
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
