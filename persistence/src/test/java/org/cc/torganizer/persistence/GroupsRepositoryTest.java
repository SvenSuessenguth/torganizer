package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GroupsRepositoryTest extends AbstractDbUnitJpaTest {

    private GroupsRepository repository;

    @BeforeEach
    public void before() throws Exception {
        super.initDatabase("test-data-groups.xml");
        repository = new GroupsRepository(entityManager);
    }

    @Test
    public void testRead_null() {
        Group g = repository.read(2L);
        assertThat(g, is(nullValue()));
    }

    @Test
    public void testRead_notNull() {
        Group g = repository.read(1L);
        assertThat(g, is(not(nullValue())));
    }

    @Test
    public void testCount() {
        long count = repository.count();
        assertThat(count, is(1L));
    }

    @Test
    public void testFilterAlreadyAssignedOpponents_Empty() {
        Round round = new Round();
        Set<Opponent> opponentsInRound = new HashSet<>();

        Set<Opponent> assignable = repository.filterAlreadyAssignedOpponents(round, opponentsInRound);

        assertThat(assignable, is(empty()));
    }

    @Test
    public void testFilterAlreadyAssignedOpponents() {
        // round with one group and two Players
        // player one is already assigned
        Player p1 = new Player("a", "b");
        Player p2 = new Player("b", "b");

        Round round = new Round();
        Group group = new Group();
        round.addGroup(group);
        group.addOpponent(p1);

        Set<Opponent> opponentsInRound = new HashSet<>();
        opponentsInRound.add(p1);
        opponentsInRound.add(p2);

        Set<Opponent> assignable = repository.filterAlreadyAssignedOpponents(round, opponentsInRound);

        assertThat(assignable, hasSize(1));

        Opponent o = assignable.iterator().next();
        assertThat(o, is(p2));
    }
}
