package org.cc.torganizer.persistence;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.cc.torganizer.core.entities.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
