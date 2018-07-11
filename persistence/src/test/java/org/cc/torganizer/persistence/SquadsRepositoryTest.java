package org.cc.torganizer.persistence;

import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SquadsRepositoryTest extends AbstractDbUnitJpaTest {

  private SquadsRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-squads.xml");
    repository = new SquadsRepository(entityManager);
  }

  @Test
  void testReadOrderdByLastName_0(){
    List<Squad> squads = repository.readOrderByLastName(0,1);

    assertThat(squads, is(not(nullValue())));
    assertThat(squads, hasSize(1));
    Squad s = squads.get(0);
    List<Player> players = new ArrayList<>(s.getPlayers());
    players.sort(new OpponentByNameComparator());

    assertThat(players.get(0).getPerson().getLastName(), is("Aöüß"));
  }

  @Test
  void testReadOrderdByLastName_1(){
    List<Squad> squads = repository.readOrderByLastName(1,1);

    assertThat(squads, is(not(nullValue())));
    assertThat(squads, hasSize(1));
    Squad s = squads.get(0);
    List<Player> players = new ArrayList<>(s.getPlayers());
    players.sort(new OpponentByNameComparator());

    assertThat(players.get(0).getPerson().getLastName(), is("nn3"));
  }

  @Test
  void testFindAll() {
    List<Squad> squads = entityManager.createNamedQuery("Squad.findAll", Squad.class)
        .getResultList();
    assertThat(squads, hasSize(2));
  }
  
  @Test
  void testById_notExisting() {
    Squad squad = repository.read(-1L);
    assertThat(squad,is(nullValue()));
  }
  
  @Test
  void testById_existing() {
    Squad squad = repository.read(5L);
    
    assertThat(squad, is(not(nullValue())));
    assertThat(squad.getPlayers(), hasSize(2));
  }
  
  @Test
  void testFindPlayers() {
    TypedQuery<Player> query = entityManager.createNamedQuery("Squad.findPlayers", Player.class);
    query.setParameter("id", 5L);
    List<Player> players = query.getResultList();
    
    assertThat(players, hasSize(2));
  }
}
