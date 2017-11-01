package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.cc.torganizer.core.entities.Player;
import org.junit.Before;
import org.junit.Test;

public class PlayersJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-players.xml");
  }

  @Test
  public void testFindAll() {
    List<Player> allPlayers = entityManager.createNamedQuery("Player.findAll", Player.class)
        .getResultList();
    assertThat(allPlayers, hasSize(2));
  }
  
}
