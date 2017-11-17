package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import javax.persistence.TypedQuery;

import org.cc.torganizer.core.entities.Club;
import org.junit.Before;
import org.junit.Test;

public class ClubsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-clubs.xml");
  }
  
  @Test
  public void testFindById_exists() {
    TypedQuery<Club> namedQuery = entityManager.createNamedQuery("Club.findById", Club.class);
    namedQuery.setParameter("id", 1L);
    
    List<Club> allClubs = namedQuery.getResultList();
    assertThat(allClubs, hasSize(1));
  }
  
  @Test
  public void testFindById_existsNot() {
    TypedQuery<Club> namedQuery = entityManager.createNamedQuery("Club.findById", Club.class);
    namedQuery.setParameter("id", -1L);
    
    List<Club> allClubs = namedQuery.getResultList();
    assertThat(allClubs, hasSize(0));
  }
  
  @Test
  public void testFindByAll() {
    TypedQuery<Club> namedQuery = entityManager.createNamedQuery("Club.findAll", Club.class);
    
    List<Club> allClubs = namedQuery.getResultList();
    assertThat(allClubs, hasSize(2));
  }
}
