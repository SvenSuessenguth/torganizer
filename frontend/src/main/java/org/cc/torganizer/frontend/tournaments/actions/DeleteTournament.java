package org.cc.torganizer.frontend.tournaments.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.tournaments.TournamentsStateSynchronizer;

/**
 * Deleting the selected Tournament.
 */
@RequestScoped
@Named
public class DeleteTournament extends TournamentsAction {

  @Inject
  private Logger logger;

  @Inject
  private TournamentsStateSynchronizer synchronizer;

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

    synchronizer.synchronize(state);

    if (Objects.equals(appState.getTournamentId(), currentTournamentId)) {
      appState.setTournament(null);
    }
  }
}
