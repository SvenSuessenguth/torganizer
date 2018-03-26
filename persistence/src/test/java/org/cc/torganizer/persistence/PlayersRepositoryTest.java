package org.cc.torganizer.persistence;

import java.util.List;
import org.cc.torganizer.core.entities.Player;
import static org.cc.torganizer.core.entities.Status.INACTIVE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

public class PlayersRepositoryTest extends AbstractDbUnitJpaTest {

  private PlayersRepository repository;

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-players.xml");

    repository = new PlayersRepository(entityManager);
  }

  @Test
  public void testGetPlayers() {

    List<Player> players = repository.getPlayers();

    assertThat(players, hasSize(2));
    // status von opponent 2 checken (inactive)
    players.stream().filter((player) -> (player.getId()==2L)).forEachOrdered((player) -> assertThat(player.getStatus(), is(INACTIVE)));
  }

  @Test
  public void testGetPlayer() {

    Player player = repository.getPlayer(2L);

    assertThat(player, is(not(nullValue())));
    assertThat(player.getPerson().getFirstName(),is("Üöä"));
    assertThat(player.getPerson().getLastName(),is("Äöüß"));
  }
}
