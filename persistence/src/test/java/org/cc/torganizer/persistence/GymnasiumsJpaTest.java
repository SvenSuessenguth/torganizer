package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.cc.torganizer.core.entities.Gymnasium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GymnasiumsJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-gymnasiums.xml");
  }

  @Test
  void testFindByTournament_withGymnasiums() {
    List<Gymnasium> allGymnasiums = entityManager.createNamedQuery("Gymnasium.findByTournament", Gymnasium.class)
        .setParameter("tournamentId", 1L)
        .getResultList();
    assertThat(allGymnasiums).hasSize(2);
  }
  
  @Test
  void testFindByTournament_withoutGymnasiums() {
    List<Gymnasium> allGymnasiums = entityManager.createNamedQuery("Gymnasium.findByTournament", Gymnasium.class)
        .setParameter("tournamentId", 2L)
        .getResultList();
    assertThat(allGymnasiums).hasSize(0);
  }
}
