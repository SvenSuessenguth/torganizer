package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.ApplicationMessages;

@RequestScoped
@Named
public class DeletePlayer extends PlayersAction {

  public static final String CLUBS_I18N_BASE_NAME = "org.cc.torganizer.frontend.players";

  @Inject
  private Logger logger;

  @Inject
  private ApplicationMessages applicationMessages;

  @Inject
  private CancelPlayer createPlayer;

  /**
   * Deleting a player is not possible, if the player has already completed any match.
   */
  public void execute() {
    logger.info("delete player");

    // delete from tournament and from players/persons-tables
    Long tournamentId = applicationState.getTournamentId();
    Player current = state.getCurrent();
    Long playerId = current.getId();

    if (tournamentId == null || playerId == null) {
      applicationMessages.addMessage(CLUBS_I18N_BASE_NAME, "error_on_delete_player",
          new Object[]{current.getPerson().getFirstName(), current.getPerson().getLastName()});
      return;
    }

    tournamentsRepository.removeOpponent(tournamentId, playerId);
    playersRepository.delete(playerId);
    state.synchronize();
  }
}
