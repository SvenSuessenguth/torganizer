package org.cc.torganizer.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchesJpaTest extends AbstractDbUnitJpaTest {

  private MatchesRepository repository;

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-matches.xml");

    repository = new MatchesRepository(entityManager);
  }

  @Test
  void testCount() {
    Long countExpected = 2L;
    Long countActual = repository.count();

    Assertions.assertThat(countActual).isEqualTo(countExpected);
  }
}
