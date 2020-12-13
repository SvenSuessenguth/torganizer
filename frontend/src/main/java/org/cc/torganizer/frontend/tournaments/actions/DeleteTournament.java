package org.cc.torganizer.frontend.tournaments.actions;

import java.util.Objects;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
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

    if (current == null || current.getId() == null) {
      return;
    }

    Long currentTournamentId = current.getId();
    logger.info("delete with name: '{}' currentTournamentId: '{}'", current.getName(),
        currentTournamentId);

    current = tournamentsRepository.read(currentTournamentId);
    tournamentsRepository.delete(current);

    state.synchronize();

    if (Objects.equals(appState.getTournamentId(), currentTournamentId)) {
      appState.setTournament(null);
    }
  }
}
