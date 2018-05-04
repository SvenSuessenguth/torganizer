package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Club;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ClubsRepositoryTest extends AbstractDbUnitJpaTest {

  private ClubsRepository repository;

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-clubs.xml");
    repository = new ClubsRepository(entityManager);
  }

  @Test
  public void testFindByAll() {
    List<Club> allClubs = repository.read(0, 10);
    assertThat(allClubs, hasSize(2));
  }
}