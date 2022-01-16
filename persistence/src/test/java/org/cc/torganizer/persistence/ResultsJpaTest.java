package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.cc.torganizer.core.entities.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultsJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-results.xml");
  }

  @Test
  void testFindAll() {
    List<Result> results = entityManager.createNamedQuery("Result.findAll", Result.class).getResultList();
    assertThat(results).hasSize(3);
  }
}
