package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultsJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-results.xml");
  }

  @Test
  void testFindAll() {
    var results = entityManager.createNamedQuery("Result.findAll", Result.class).getResultList();
    assertThat(results).hasSize(3);
  }
}
