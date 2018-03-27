package org.cc.torganizer.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Before;
import org.junit.Test;

public class SquadsRepositoryTest extends AbstractDbUnitJpaTest {

  private SquadsRepository repository;

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-squads.xml");
    repository = new SquadsRepository(entityManager);
  }

  @Test
  public void testReadOrderdByLastName_0(){
    List<Squad> squads = repository.readOrderByLastName(0,1);

    assertThat(squads, is(not(nullValue())));
    assertThat(squads, hasSize(1));
    Squad s = squads.get(0);
    List<Player> players = new ArrayList<>(s.getPlayers());
    Collections.sort(players, new OpponentByNameComparator());

    assertThat(players.get(0).getPerson().getLastName(), is("Aöüß"));
  }

  @Test
  public void testReadOrderdByLastName_1(){
    List<Squad> squads = repository.readOrderByLastName(1,1);

    assertThat(squads, is(not(nullValue())));
    assertThat(squads, hasSize(1));
    Squad s = squads.get(0);
    List<Player> players = new ArrayList<>(s.getPlayers());
    Collections.sort(players, new OpponentByNameComparator());

    assertThat(players.get(0).getPerson().getLastName(), is("nn3"));
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
  
  @Test
  public void testFindPlayers() {
    Query query = entityManager.createNamedQuery("Squad.findPlayers", Player.class);
    query.setParameter("id", 5L);
    List<Player> players = query.getResultList();
    
    assertThat(players, hasSize(2));
  }
}
