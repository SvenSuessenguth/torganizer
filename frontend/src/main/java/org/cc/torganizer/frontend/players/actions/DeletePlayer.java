package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;

@RequestScoped
@Named
public class DeletePlayer extends PlayersAction {

  @Inject
  private Logger logger;

  /**
   * Deleting a player is not possible, if the player has already completed any match.
   */
  public void execute() {
    logger.info("delete player");

    // delete from tournament and from players/persons-tables
    Long tournamentId = applicationState.getTournamentId();
    Long playerId = state.getCurrent().getId();

    if (tournamentId == null || playerId == null) {
      FacesMessage message = new FacesMessage("Fehler beim Löschen eines Players");
      FacesContext.getCurrentInstance().addMessage(null, message);
      return;
    }

    tournamentsRepository.removePlayer(tournamentId, playerId);
    playersRepository.delete(playerId);
    state.synchronize();
  }
}
