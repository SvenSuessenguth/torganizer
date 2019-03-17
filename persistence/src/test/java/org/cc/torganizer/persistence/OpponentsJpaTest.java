package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.cc.torganizer.core.entities.Opponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpponentsJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-opponents.xml");
  }

  @Test
  void testFindAll() {
    List<Opponent> opponents = entityManager.createNamedQuery("Opponent.findAll", Opponent.class)
        .getResultList();
    assertThat(opponents).hasSize(2);
  }
}
