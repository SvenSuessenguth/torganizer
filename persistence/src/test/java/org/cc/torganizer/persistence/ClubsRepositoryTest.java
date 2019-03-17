package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.cc.torganizer.core.entities.Club;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClubsRepositoryTest extends AbstractDbUnitJpaTest {

  private ClubsRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-clubs.xml");
    repository = new ClubsRepository(entityManager);
  }

  @Test
  void testFindByAll() {
    List<Club> allClubs = repository.read(0, 10);
    assertThat(allClubs).hasSize(2);
  }
}
