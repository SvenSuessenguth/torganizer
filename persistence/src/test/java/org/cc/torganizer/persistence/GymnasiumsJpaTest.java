package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.cc.torganizer.core.entities.Gymnasium;
import org.junit.Before;
import org.junit.Test;

public class GymnasiumsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-gymnasiums.xml");
  }

  @Test
  public void testFindByTournament_withGymnasiums() {
    List<Gymnasium> allGymnasiums = entityManager.createNamedQuery("Gymnasium.findByTournament", Gymnasium.class)
        .setParameter("tournamentId", 1L)
        .getResultList();
    assertThat(allGymnasiums, hasSize(2));
  }
  
  @Test
  public void testFindByTournament_withoutGymnasiums() {
    List<Gymnasium> allGymnasiums = entityManager.createNamedQuery("Gymnasium.findByTournament", Gymnasium.class)
        .setParameter("tournamentId", 2L)
        .getResultList();
    assertThat(allGymnasiums, hasSize(0));
  }
}
