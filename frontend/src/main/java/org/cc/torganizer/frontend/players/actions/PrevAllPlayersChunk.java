package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * Setting the state to the previous chunk of players.
 */
@RequestScoped
@Named
public class PrevAllPlayersChunk extends PlayersAction {

  /**
   * Execute.
   */
  public void execute() {
    var chunkIndex = state.getAllPlayersChunkIndex();
    if (chunkIndex > 0) {
      state.setAllPlayersChunkIndex(chunkIndex - 1);
    }
  }
}