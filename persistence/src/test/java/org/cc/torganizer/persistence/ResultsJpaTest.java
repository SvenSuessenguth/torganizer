package org.cc.torganizer.persistence;

import org.assertj.core.api.Assertions;
import org.cc.torganizer.core.entities.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ResultsJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-results.xml");
  }

  @Test
  void testFindAll() {
    List<Result> results = entityManager.createNamedQuery("Result.findAll", Result.class).getResultList();
    Assertions.assertThat(results).hasSize(3);
  }
}
