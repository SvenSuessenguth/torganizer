package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.core.entities.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TournamentsRepositoryTest extends AbstractDbUnitJpaTest {

  private TournamentsRepository repository;

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-tournament.xml");

    repository = new TournamentsRepository(entityManager);
  }

  @Test
  void testCriteriaListPlayersOrderedByLastName() {
    var players = repository.getPlayersOrderedByLastName(1L, 0, 5);

    assertThat(players.getFirst().getPerson().getLastName()).isEqualTo("Aöüß");
  }

  @Test
  void testCriteriaFindPlayers1000() {
    var players = repository.getPlayers(1L, 0, 1000);

    assertThat(players).hasSize(2);
  }

  @Test
  void testCriteriaFindPlayers1() {
    var players = repository.getPlayers(1L, 0, 1);

    assertThat(players).hasSize(1);
  }

  @Test
  void testGetTournament() {
    var t = repository.read(1L);

    assertThat(t).isNotNull();
  }

  @Test
  void testGetTournaments() {
    var tournaments = repository.read(0, 10);

    assertThat(tournaments).hasSize(2);
  }

  @Test
  void testGetTournaments_usingMaxResults() {
    var tournaments = repository.read(0, 1);

    assertThat(tournaments).hasSize(1);
  }

  @Test
  void testCountOpponents() {
    var query = entityManager.createNamedQuery("Tournament.countOpponents");
    query.setParameter("id", 1L);
    var count = query.getSingleResult();

    assertThat(count).isEqualTo(3L);
  }

  @Test
  void testAddPlayer() {
    var countBefore = repository.countPlayers(1L);
    repository.addOpponent(1L, 6L);
    var countAfter = repository.countPlayers(1L);

    assertThat(countBefore).isEqualTo(countAfter - 1);
  }

  @Test
  void testCountPlayers_existingTournament() {
    var query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", 1L);
    var countSubscribers = query.getSingleResult();

    assertThat(countSubscribers).isEqualTo(2L);
  }

  @Test
  void testCountSubscribers_nonExistingTournament() {
    var query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", 3L);
    var countSubscribers = (long) query.getSingleResult();

    assertThat(countSubscribers).isZero();
  }

  @Test
  void testCountSubscribers_nonExistingTournament_NullId() {
    var query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", null);
    var countSubscribers = (long) query.getSingleResult();

    assertThat(countSubscribers).isZero();
  }

  @Test
  void testFindPlayers() {
    var query = entityManager.createNamedQuery("Tournament.findPlayers", Player.class);
    query.setParameter("id", 1L);
    var players = query.getResultList();

    assertThat(players).hasSize(2);
  }

  @Test
  void testFindSquads() {
    var query = entityManager.createNamedQuery("Tournament.findSquads", Squad.class);
    query.setParameter("id", 1L);
    var squads = query.getResultList();

    assertThat(squads).hasSize(1);
  }

  @Test
  void testFindPlayers_none() {
    var query = entityManager.createNamedQuery("Tournament.findPlayers", Player.class);
    query.setParameter("id", 3L);
    var players = query.getResultList();

    assertThat(players).isEmpty();
  }

  @Test
  void testFindSquads_none() {
    var query = entityManager.createNamedQuery("Tournament.findSquads", Squad.class);
    query.setParameter("id", 2L);
    var squads = query.getResultList();

    assertThat(squads).isEmpty();
  }

  @Test
  void testFindDisciplines() {
    var query = entityManager.createNamedQuery("Tournament.findDisciplines", Discipline.class);
    query.setParameter("id", 1L);
    var disciplines = query.getResultList();

    assertThat(disciplines).hasSize(1);
  }

  @Test
  void read_defaults() {
    var read = repository.read(null, null);
    assertThat(read).isNotEmpty();
  }

  @Test
  void removeOpponent() {
    var opponent = repository.removeOpponent(1L, 1L);
    assertThat(opponent).isNotNull();
  }

  @Test
  void removeDiscipline() {
    var discipline = repository.removeDiscipline(1L, 1L);
    assertThat(discipline).isNotNull();
  }

  @Test
  void getSquads() {
    var squads = repository.getSquads(1L, 0, 10);
    assertThat(squads).hasSize(1);
  }

  @Test
  void getSquads_none() {
    var squads = repository.getSquads(2L, 0, 10);
    assertThat(squads).isEmpty();
  }

  @Test
  void getSquads_defaults() {
    var squads = repository.getSquads(1L, null, null);
    assertThat(squads).hasSize(1);
  }

  @Test
  void saveTournament_nameAlreadyExisting() {
    var existingName = "Testturnier-1";
    var tournament = new Tournament();
    tournament.setName(existingName);

    assertThatThrownBy(() -> repository.create(tournament)).isInstanceOf(CreateEntityException.class);
  }
}
