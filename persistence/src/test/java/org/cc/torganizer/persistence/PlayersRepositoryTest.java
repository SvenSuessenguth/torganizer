package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Status.INACTIVE;

class PlayersRepositoryTest extends AbstractDbUnitJpaTest {

  private PlayersRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-players.xml");

    repository = new PlayersRepository(entityManager);
  }

  @Test
  void testGetPlayers() {

    List<Player> players = repository.read(null, null);

    assertThat(players).hasSize(2);
    // status von opponent 2 checken (inactive)
    players.stream().filter((player) -> (player.getId() == 2L)).forEachOrdered((player) -> assertThat(player.getStatus()).isEqualTo(INACTIVE));
  }

  @Test
  void testGetPlayer() {

    Player player = repository.read(2L);

    assertThat(player).isNotNull();
    assertThat(player.getPerson().getFirstName()).isEqualTo("Üöä");
    assertThat(player.getPerson().getLastName()).isEqualTo("Äöüß");
  }

  @Test
  void testPlayersClubIsNotNull(){
    Player player = repository.read(1L);
    Club club = player.getClub();

    assertThat(club).isNotNull();
  }

  @Test
  void testPlayersClubIsNull(){
    Player player = repository.read(2L);
    Club club = player.getClub();

    assertThat(club).isNull();
  }
}
