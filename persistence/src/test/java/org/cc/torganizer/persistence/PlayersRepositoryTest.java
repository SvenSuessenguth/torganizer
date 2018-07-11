package org.cc.torganizer.persistence;

import java.util.List;

import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.cc.torganizer.core.entities.Status.INACTIVE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PlayersRepositoryTest extends AbstractDbUnitJpaTest {

    private PlayersRepository repository;

    @BeforeEach
    public void before() throws Exception {
        super.initDatabase("test-data-players.xml");

        repository = new PlayersRepository(entityManager);
    }

    @Test
    public void testGetPlayers() {

        List<Player> players = repository.read(null, null);

        assertThat(players, hasSize(2));
        // status von opponent 2 checken (inactive)
        players.stream().filter((player) -> (player.getId() == 2L)).forEachOrdered((player) -> assertThat(player.getStatus(), is(INACTIVE)));
    }

    @Test
    public void testGetPlayer() {

        Player player = repository.read(2L);

        assertThat(player, is(not(nullValue())));
        assertThat(player.getPerson().getFirstName(), is("Üöä"));
        assertThat(player.getPerson().getLastName(), is("Äöüß"));
    }

    @Test
    public void testPlayersClubIsNotNull() {
        Player player = repository.read(1L);
        Club club = player.getClub();

        assertThat(club, is(not(nullValue())));
    }

    @Test
    public void testPlayersClubIsNull() {
        Player player = repository.read(2L);
        Club club = player.getClub();

        assertThat(club, is(nullValue()));
    }
}
