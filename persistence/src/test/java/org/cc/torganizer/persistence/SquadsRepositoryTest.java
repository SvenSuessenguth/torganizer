package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.TypedQuery;
import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SquadsRepositoryTest extends AbstractDbUnitJpaTest {

  private SquadsRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-squads.xml");
    repository = new SquadsRepository(entityManager);
  }

  @Test
  void testCreate() {
    long countBefore = repository.count();

    Squad squad = new Squad();
    squad = repository.create(squad);
    Long squadId = squad.getId();

    repository.addPlayer(squadId, 1L);
    repository.addPlayer(squadId, 2L);

    long countAfter = repository.count();

    assertThat(countAfter).isEqualTo(countBefore + 1L);
    assertThat(squadId).isNotNull();
  }

  @Test
  void testAddPlayer(){
    // Folgender Eintrag fehlt bei den Testdaten
    // Mit diesem Test soll der bestehende Player 2
    // dem bestehenden Squad 5 hinzugefügt werdem
    // <_SQUAD_PLAYERS _SQUAD_ID="5" _PLAYER_ID="2" />
    repository.addPlayer(5L, 2L);
    Squad squad = repository.read(5L);

    assertThat(squad.getPlayers()).hasSize(2);
  }

  @Test
  void testReadOrderdByLastName_0() {
    List<Squad> squads = repository.readOrderByLastName(0, 1);

    assertThat(squads).hasSize(1);
    Squad s = squads.get(0);
    List<Player> players = new ArrayList<>(s.getPlayers());
    players.sort(new OpponentByNameComparator());

    assertThat(players.get(0).getPerson().getLastName()).isEqualTo("Meier");
  }

  @Test
  void testReadOrderdByLastName_1() {
    List<Squad> squads = repository.readOrderByLastName(1, 1);

    assertThat(squads).hasSize(1);
    Squad s = squads.get(0);
    List<Player> players = new ArrayList<>(s.getPlayers());
    players.sort(new OpponentByNameComparator());

    assertThat(players.get(0).getPerson().getLastName()).isEqualTo("nn3");
  }

  @Test
  void testFindAll() {
    List<Squad> squads = entityManager.createNamedQuery("Squad.findAll", Squad.class)
        .getResultList();
    assertThat(squads).hasSize(2);
  }

  @Test
  void testById_notExisting() {
    Squad squad = repository.read(-1L);
    assertThat(squad).isNull();
  }

  @Test
  void testById_existing() {
    Squad squad = repository.read(5L);

    assertThat(squad).isNotNull();
    assertThat(squad.getPlayers()).hasSize(1);
  }

  @Test
  void testFindPlayers() {
    TypedQuery<Player> query = entityManager.createNamedQuery("Squad.findPlayers", Player.class);
    query.setParameter("id", 5L);
    List<Player> players = query.getResultList();

    assertThat(players).hasSize(1);
  }
}
