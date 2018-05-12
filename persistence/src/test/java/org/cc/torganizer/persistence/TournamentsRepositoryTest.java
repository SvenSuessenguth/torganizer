package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TournamentsRepositoryTest extends AbstractDbUnitJpaTest {

  private TournamentsRepository repository;

  @BeforeEach
  public void before() throws Exception {
    super.initDatabase("test-data-tournament.xml");

    repository = new TournamentsRepository(entityManager);
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
    List<Player> players = repository.getPlayersOrderedByLastName(1L, 0, 5);

    assertThat(players.get(0).getPerson().getLastName(), is("Aöüß"));
  }

  @Test
  public void testGetTournament(){
    Tournament t = repository.read(1L);

    assertThat(t, is(not(nullValue())));
  }

  @Test
  public void testGetTournaments(){
    List<Tournament> tournaments = repository.getTournaments(0,10);

    assertThat(tournaments, hasSize(2));
  }

  @Test
  public void testGetTournaments_usingMaxResults(){
    List<Tournament> tournaments = repository.getTournaments(0,1);

    assertThat(tournaments, hasSize(1));
  }

  @Test
  public void testAddPlayer(){
    long countBefore = repository.countPlayers(1L);
    Player p = repository.addPlayer(1L, 6L);
    long countAfter = repository.countPlayers(1L);

    assertThat(countBefore, is(countAfter-1));
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
