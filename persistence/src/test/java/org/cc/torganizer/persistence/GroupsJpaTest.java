package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import javax.persistence.TypedQuery;

import org.cc.torganizer.core.entities.Group;
import org.junit.Before;
import org.junit.Test;

public class GroupsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-groups.xml");
  }
  
  @Test
  public void testFindById_exists() {
    TypedQuery<Group> namedQuery = entityManager.createNamedQuery("Group.findById", Group.class);
    namedQuery.setParameter("id", 1L);
    
    List<Group> allGroups = namedQuery.getResultList();
    assertThat(allGroups, hasSize(1));
    Group g = allGroups.get(0);
    
    assertThat(g.getMatches(), hasSize(1));
    assertThat(g.getPositionalOpponents(), hasSize(2));
  }
}
