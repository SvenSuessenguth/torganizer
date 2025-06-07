package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.core.entities.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TournamentsRepositoryTest extends AbstractDbUnitJpaTest {

  private TournamentsRepository repository;

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-tournament.xml");

    repository = new TournamentsRepository(entityManager);
  }

  @Test
  void testCriteriaListPlayersOrderedByLastName() {
    List<Player> players = repository.getPlayersOrderedByLastName(1L, 0, 5);

    assertThat(players.getFirst().getPerson().getLastName()).isEqualTo("Aöüß");
  }

  @Test
  void testCriteriaFindPlayers1000() {
    List<Player> players = repository.getPlayers(1L, 0, 1000);

    assertThat(players).hasSize(2);
  }

  @Test
  void testCriteriaFindPlayers1() {
    List<Player> players = repository.getPlayers(1L, 0, 1);

    assertThat(players).hasSize(1);
  }

  @Test
  void testGetTournament() {
    Tournament t = repository.read(1L);

    assertThat(t).isNotNull();
  }

  @Test
  void testGetTournaments() {
    List<Tournament> tournaments = repository.read(0, 10);

    assertThat(tournaments).hasSize(2);
  }

  @Test
  void testGetTournaments_usingMaxResults() {
    List<Tournament> tournaments = repository.read(0, 1);

    assertThat(tournaments).hasSize(1);
  }

  @Test
  void testCountOpponents() {
    Query query = entityManager.createNamedQuery("Tournament.countOpponents");
    query.setParameter("id", 1L);
    Long count = (Long) query.getSingleResult();

    assertThat(count).isEqualTo(3L);
  }

  @Test
  void testAddPlayer() {
    long countBefore = repository.countPlayers(1L);
    repository.addOpponent(1L, 6L);
    long countAfter = repository.countPlayers(1L);

    assertThat(countBefore).isEqualTo(countAfter - 1);
  }

  @Test
  void testCountPlayers_existingTournament() {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", 1L);
    long countSubscribers = (long) query.getSingleResult();

    assertThat(countSubscribers).isEqualTo(2L);
  }

  @Test
  void testCountSubscribers_nonExistingTournament() {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", 3L);
    long countSubscribers = (long) query.getSingleResult();

    assertThat(countSubscribers).isZero();
  }

  @Test
  void testCountSubscribers_nonExistingTournament_NullId() {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", null);
    long countSubscribers = (long) query.getSingleResult();

    assertThat(countSubscribers).isZero();
  }

  @Test
  void testFindPlayers() {
    TypedQuery<Player> query = entityManager.createNamedQuery("Tournament.findPlayers", Player.class);
    query.setParameter("id", 1L);
    List<Player> players = query.getResultList();

    assertThat(players).hasSize(2);
  }

  @Test
  void testFindSquads() {
    TypedQuery<Squad> query = entityManager.createNamedQuery("Tournament.findSquads", Squad.class);
    query.setParameter("id", 1L);
    List<Squad> squads = query.getResultList();

    assertThat(squads).hasSize(1);
  }

  @Test
  void testFindPlayers_none() {
    TypedQuery<Player> query = entityManager.createNamedQuery("Tournament.findPlayers", Player.class);
    query.setParameter("id", 3L);
    List<Player> players = query.getResultList();

    assertThat(players).isEmpty();
  }

  @Test
  void testFindSquads_none() {
    TypedQuery<Squad> query = entityManager.createNamedQuery("Tournament.findSquads", Squad.class);
    query.setParameter("id", 2L);
    List<Squad> squads = query.getResultList();

    assertThat(squads).isEmpty();
  }

  @Test
  void testFindDisciplines() {
    TypedQuery<Discipline> query = entityManager.createNamedQuery("Tournament.findDisciplines", Discipline.class);
    query.setParameter("id", 1L);
    List<Discipline> disciplines = query.getResultList();

    assertThat(disciplines).hasSize(1);
  }

  @Test
  void read_defaults() {
    List<Tournament> read = repository.read(null, null);
    assertThat(read).isNotEmpty();
  }

  @Test
  void removeOpponent() {
    Opponent opponent = repository.removeOpponent(1L, 1L);
    assertThat(opponent).isNotNull();
  }

  @Test
  void removeDiscipline() {
    Discipline discipline = repository.removeDiscipline(1L, 1L);
    assertThat(discipline).isNotNull();
  }

  @Test
  void getSquads() {
    List<Squad> squads = repository.getSquads(1L, 0, 10);
    assertThat(squads).hasSize(1);
  }

  @Test
  void getSquads_none() {
    List<Squad> squads = repository.getSquads(2L, 0, 10);
    assertThat(squads).isEmpty();
  }

  @Test
  void getSquads_defaults() {
    List<Squad> squads = repository.getSquads(1L, null, null);
    assertThat(squads).hasSize(1);
  }

  @Test
  void saveTournament_nameAlreadyExisting() {
    var existingName = "Testturnier-1";
    Tournament tournament = new Tournament();
    tournament.setName(existingName);

    assertThatThrownBy(() -> repository.create(tournament)).isInstanceOf(CreateEntityException.class);
  }
}
