package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Club;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClubsRepositoryTest extends AbstractDbUnitJpaTest {

  private ClubsRepository repository;

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-clubs.xml");
    repository = new ClubsRepository(entityManager);
  }

  @Test
  void testFindByAll() {
    var allClubs = repository.read(0, 10);
    assertThat(allClubs).hasSize(2);
  }

  @Test
  void testFindPlayers() {
    var club = new Club();
    club.setId(1L);
    var players = repository.getPlayers(club);

    assertThat(players).hasSize(2);
  }

  @Test
  void testFindPlayersNone() {
    var club = new Club();
    club.setId(2L);
    var players = repository.getPlayers(club);

    assertThat(players).isEmpty();
  }

  @Test
  void testCountPlayers() {
    var club = new Club();
    club.setId(1L);
    var count = repository.countPlayers(club);

    assertThat(count).isEqualTo(2);
  }

  @Test
  void testCountPlayersNone() {
    var club = new Club();
    club.setId(2L);
    var count = repository.countPlayers(club);

    assertThat(count).isZero();
  }
}
