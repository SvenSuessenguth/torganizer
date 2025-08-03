package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Court;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CourtsJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  void beforeAll() throws Exception {
    super.initDatabase("test-data-courts.xml");
  }

  @Test
  void testFindAll() {
    var namedQuery = entityManager.createNamedQuery("Court.findAll", Court.class);

    var allCourts = namedQuery.getResultList();
    assertThat(allCourts).hasSize(2);
  }
}
