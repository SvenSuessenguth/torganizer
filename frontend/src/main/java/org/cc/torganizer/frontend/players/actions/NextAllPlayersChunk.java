package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.players.PlayersState;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Setting the state to the next chunk of players.
 */
@RequestScoped
@Named
public class NextAllPlayersChunk {

  @Inject
  protected PlayersState state;

  /**
   * Executing.
   */
  public void execute() {
    int chunkIndex = state.getAllPlayersChunkIndex();
    int allPlayersCount = state.getPlayers().size();

    if ((chunkIndex + 1) * PlayersState.ALL_PLAYERS_CHUNK_SIZE > allPlayersCount) {
      return;
    }

    state.setAllPlayersChunkIndex(chunkIndex + 1);
  }
}
