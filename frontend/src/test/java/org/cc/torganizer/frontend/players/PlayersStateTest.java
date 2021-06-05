package org.cc.torganizer.frontend.players;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.frontend.players.PlayersState.ALL_PLAYERS_CHUNK_SIZE;

import java.util.Collection;
import java.util.Collections;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}