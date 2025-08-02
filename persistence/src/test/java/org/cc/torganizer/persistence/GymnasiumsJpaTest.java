package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Gymnasium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GymnasiumsJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-gymnasiums.xml");
  }

  @Test
  void testFindByTournament_withGymnasiums() {
    var allGymnasiums = entityManager.createNamedQuery("Gymnasium.findByTournament", Gymnasium.class)
      .setParameter("tournamentId", 1L)
      .getResultList();
    assertThat(allGymnasiums).hasSize(2);
  }

  @Test
  void testFindByTournament_withoutGymnasiums() {
    var allGymnasiums = entityManager.createNamedQuery("Gymnasium.findByTournament", Gymnasium.class)
      .setParameter("tournamentId", 2L)
      .getResultList();
    assertThat(allGymnasiums).isEmpty();
  }
}
