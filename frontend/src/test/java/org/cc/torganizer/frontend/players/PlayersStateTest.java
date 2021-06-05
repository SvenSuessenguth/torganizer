package org.cc.torganizer.frontend.players;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.frontend.players.PlayersState.ALL_PLAYERS_CHUNK_SIZE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PlayersStateTest {

  private PlayersState state;

  @BeforeEach
  public void beforeEach() {
    state = new PlayersState();
  }

  @Test
  void testGetPlayersChunk_noPlayers_updatedWithEmptyPlayers() {
    state.setPlayers(Collections.emptyList());
    Collection<Player> playersChunk = state.getPlayersChunk();

    assertThat(playersChunk).hasSize(ALL_PLAYERS_CHUNK_SIZE);
  }

  @Test
  void testSetCurrentClubId_null() {
    Club c0 = new Club();
    c0.setId(0L);
    Club c1 = new Club();
    c1.setId(1L);
    List<Club> clubs = Arrays.asList(c0, c1);
    state.setClubs(clubs);
    state.setCurrent(new Player());

    state.setCurrentPlayersClubById(null);

    Player player = state.getCurrent();
    assertThat(player.getClub()).isNull();
  }

  @Test
  void testSetCurrentClubId_existing() {
    Club c0 = new Club();
    c0.setId(0L);
    Club c1 = new Club();
    c1.setId(1L);
    List<Club> clubs = Arrays.asList(c0, c1);
    state.setClubs(clubs);
    state.setCurrent(new Player());

    state.setCurrentPlayersClubById(1L);

    Player player = state.getCurrent();
    Club club = player.getClub();
    assertThat(club).isNotNull();
    assertThat(club.getId()).isEqualTo(1L);
  }

  @Test
  void testSetCurrentClubId_notExisting() {
    Club c0 = new Club();
    c0.setId(0L);
    Club c1 = new Club();
    c1.setId(1L);
    List<Club> clubs = Arrays.asList(c0, c1);
    state.setClubs(clubs);
    state.setCurrent(new Player());

    state.setCurrentPlayersClubById(3L);

    Player player = state.getCurrent();
    Club club = player.getClub();
    assertThat(club).isNull();
  }

  public static Stream<Arguments> testIsNextAllPlayersChunkAvailableArguments() {
    return Stream.of(
        Arguments.of(0, 0, false),
        Arguments.of(10, 0, false),
        Arguments.of(11, 0, true),
        Arguments.of(11, 1, false),
        Arguments.of(20, 1, false),
        Arguments.of(21, 1, true)
    );
  }

  @ParameterizedTest
  @MethodSource("testIsNextAllPlayersChunkAvailableArguments")
  void testIsNextAllPlayersChunkAvailable(int playersSize, int chunkIndex, boolean expected) {

    // prepare list of players
    List<Player> players = new ArrayList<>();
    for (int counter = 0; counter < playersSize; counter++) {
      players.add(new Player());
    }
    state.setPlayers(players);
    state.setAllPlayersChunkIndex(chunkIndex);

    boolean actual = state.isNextAllPlayersChunkAvailable();

    assertThat(actual).isEqualTo(expected);
  }
}