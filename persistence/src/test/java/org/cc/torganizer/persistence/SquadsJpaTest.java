package org.cc.torganizer.persistence;

import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.cc.torganizer.core.entities.Squad;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Before;
import org.junit.Test;

public class SquadsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-squads.xml");
  }

  @Test
  public void testFindAll() {
    List<Squad> squads = entityManager.createNamedQuery("Squad.findAll", Squad.class)
        .getResultList();
    assertThat(squads, hasSize(2));
  }
  
  @Test(expected = NoResultException.class)
  public void testById_notExisting() {
    final TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findById", Squad.class);
    namedQuery.setParameter("id", -1L);
    
    namedQuery.getSingleResult();
  }
  
  @Test
  public void testById_existing() {
    final TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findById", Squad.class);
    namedQuery.setParameter("id", 5L);
    
    Squad squad = namedQuery.getSingleResult();
    
    assertThat(squad, is(not(nullValue())));
    assertThat(squad.getPlayers(), hasSize(2));
  }
}
