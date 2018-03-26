package org.cc.torganizer.persistence;

import java.util.List;
import org.cc.torganizer.core.entities.Player;
import static org.cc.torganizer.core.entities.Status.INACTIVE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;

public class PlayersRepositoryTest extends AbstractDbUnitJpaTest {

  private PlayerRepository repository;

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-players.xml");

    repository = new PlayerRepository(entityManager);
  }

  @Test
  public void testFindAll() {

    List<Player> players = repository.getAll();

    assertThat(players, hasSize(2));
    // status von opponent 2 checken (inactive)
    players.stream().filter((player) -> (player.getId()==2L)).forEachOrdered((player) -> assertThat(player.getStatus(), is(INACTIVE)));
  }
}
