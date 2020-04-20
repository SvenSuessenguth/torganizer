package org.cc.torganizer.frontend.tournaments.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Tournament;

@RequestScoped
@Named
public class DeleteTournament extends TournamentsAction {

  @Inject
  private Logger logger;

  /**
   * deleting a tournament.
   */
  @Transactional
  public void execute() {
    Tournament current = state.getCurrent();
    Long currentTournamentId = current.getId();
    if (current == null || currentTournamentId == null) {
      return;
    }

    logger.info("delete with name: '{}' currentTournamentId: '{}'", current.getName(), currentTournamentId);

    current = tournamentsRepository.read(currentTournamentId);
    tournamentsRepository.delete(current);

    state.synchronize();

    if(appState.getTournamentId()==currentTournamentId) {
      appState.setTournament(null);
    }
  }
}
