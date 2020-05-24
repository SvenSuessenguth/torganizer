package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.frontend.players.PlayersState;

@RequestScoped
@Named
public class NextAllPlayersChunk extends PlayersAction {

  public void execute() {
    int chunkIndex = state.getAllPlayersChunkIndex();
    int allPlayersCount = state.getPlayers().size();

    if ((chunkIndex + 1) * PlayersState.ALL_PLAYERS_CHUNK_SIZE > allPlayersCount) {
      return;
    }

    state.setAllPlayersChunkIndex(chunkIndex + 1);
  }
}
