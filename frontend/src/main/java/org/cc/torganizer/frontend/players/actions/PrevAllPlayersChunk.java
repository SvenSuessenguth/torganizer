package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class PrevAllPlayersChunk extends PlayersAction {

  public void execute() {
    int chunkIndex = state.getAllPlayersChunkIndex();
    if (chunkIndex > 0) {
      state.setAllPlayersChunkIndex(chunkIndex - 1);
    }
  }
}