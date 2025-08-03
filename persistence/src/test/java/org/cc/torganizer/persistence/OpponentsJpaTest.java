package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Opponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpponentsJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  void beforeAll() throws Exception {
    super.initDatabase("test-data-opponents.xml");
  }

  @Test
  void testFindAll() {
    var opponents = entityManager.createNamedQuery("Opponent.findAll", Opponent.class)
      .getResultList();
    assertThat(opponents).hasSize(2);
  }
}
