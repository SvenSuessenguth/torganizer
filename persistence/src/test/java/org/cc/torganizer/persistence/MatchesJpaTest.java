package org.cc.torganizer.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchesJpaTest extends AbstractDbUnitJpaTest {

  private MatchesRepository repository;

  @BeforeEach
  void beforeAll() throws Exception {
    super.initDatabase("test-data-matches.xml");

    repository = new MatchesRepository(entityManager);
  }

  @Test
  void testCount() {
    var countExpected = 2L;
    var countActual = repository.count();

    assertThat(countActual).isEqualTo(countExpected);
  }
}
