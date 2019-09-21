package org.cc.torganizer.persistence;

import org.assertj.core.api.Assertions;
import org.cc.torganizer.core.entities.Court;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import java.util.List;

class CourtsJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-courts.xml");
  }

  @Test
  void testFindAll() {
    TypedQuery<Court> namedQuery = entityManager.createNamedQuery("Court.findAll", Court.class);
    
    List<Court> allCourts = namedQuery.getResultList();
    Assertions.assertThat(allCourts).hasSize(2);
  }
}
