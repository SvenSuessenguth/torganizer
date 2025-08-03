package org.cc.torganizer.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Status.INACTIVE;

class PlayersRepositoryTest extends AbstractDbUnitJpaTest {

  private PlayersRepository repository;

  @BeforeEach
  void beforeAll() throws Exception {
    super.initDatabase("test-data-players.xml");

    repository = new PlayersRepository(entityManager);
  }

  @Test
  void testGetPlayers() {

    var players = repository.read(null, null);

    assertThat(players).hasSize(2);
    // status von opponent 2 checken (inactive)
    players.stream().filter(player -> (player.getId() == 2L)).forEachOrdered(player -> assertThat(player.getStatus()).isEqualTo(INACTIVE));
  }

  @Test
  void testGetPlayer() {

    var player = repository.read(2L);

    assertThat(player).isNotNull();
    assertThat(player.getPerson().getFirstName()).isEqualTo("Üöä");
    assertThat(player.getPerson().getLastName()).isEqualTo("Äöüß");
  }

  @Test
  void testPlayersClubIsNotNull() {
    var player = repository.read(1L);
    var club = player.getClub();

    assertThat(club).isNotNull();
  }

  @Test
  void testPlayersClubIsNull() {
    var player = repository.read(2L);
    var club = player.getClub();

    assertThat(club).isNull();
  }
}
