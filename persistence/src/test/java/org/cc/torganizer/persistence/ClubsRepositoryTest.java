package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClubsRepositoryTest extends AbstractDbUnitJpaTest {

  private ClubsRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-clubs.xml");
    repository = new ClubsRepository(entityManager);
  }

  @Test
  void testFindByAll() {
    List<Club> allClubs = repository.read(0, 10);
    assertThat(allClubs).hasSize(2);
  }

  @Test
  void testFindPlayers() {
    Club club = new Club();
    club.setId(1L);
    List<Player> players = repository.getPlayers(club);

    assertThat(players).hasSize(2);
  }

  @Test
  void testFindPlayersNone() {
    Club club = new Club();
    club.setId(2L);
    List<Player> players = repository.getPlayers(club);

    assertThat(players).isEmpty();
  }

  @Test
  void testCountPlayers() {
    Club club = new Club();
    club.setId(1L);
    Long count = repository.countPlayers(club);

    assertThat(count).isEqualTo(2);
  }

  @Test
  void testCountPlayersNone() {
    Club club = new Club();
    club.setId(2L);
    Long count = repository.countPlayers(club);

    assertThat(count).isZero();
  }
}
