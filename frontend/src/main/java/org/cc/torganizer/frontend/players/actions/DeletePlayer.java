package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;

@RequestScoped
@Named
public class DeletePlayer extends PlayersAction {

  @Inject
  private Logger logger;

  /**
   * Deleting a player is not possible, if the player has already completed any match
   */
  public void execute() {
    logger.info("delete player");

    // delete from tournament and from players/persons-tables
    Long tId = appState.getCurrent().getId();
    Long pId = state.getCurrent().getId();

    tournamentsRepository.removePlayer(tId, pId);
    playersRepository.delete(pId);
    initPlayerState.execute(true);
  }
}
