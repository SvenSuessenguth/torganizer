package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Club;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ClubsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-clubs.xml");
  }

  @Test
  public void testFindByAll() {
    TypedQuery<Club> namedQuery = entityManager.createNamedQuery("Club.findAll", Club.class);
    
    List<Club> allClubs = namedQuery.getResultList();
    assertThat(allClubs, hasSize(2));
  }
}
