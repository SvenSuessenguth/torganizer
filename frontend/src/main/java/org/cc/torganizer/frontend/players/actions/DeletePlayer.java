package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.ApplicationMessages;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Deleting selected Player.
 */
@RequestScoped
@Named
public class DeletePlayer extends PlayersAction {

  public static final String CLUBS_I18N_BASE_NAME = "org.cc.torganizer.frontend.players";

  @Inject
  private Logger logger;

  @Inject
  protected TournamentsRepository tournamentsRepository;

  @Inject
  private ApplicationMessages applicationMessages;

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
    repository.delete(playerId);
    synchronizer.synchronize(state);
  }
}
