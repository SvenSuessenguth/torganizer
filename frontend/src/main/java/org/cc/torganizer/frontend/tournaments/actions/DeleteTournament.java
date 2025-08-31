package org.cc.torganizer.frontend.tournaments.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.frontend.tournaments.TournamentsStateSynchronizer;
import org.cc.torganizer.persistence.TournamentsRepository;

import java.util.Objects;

/**
 * Deleting the selected Tournament.
 */
@RequestScoped
@Named
@Slf4j
public class DeleteTournament {
  @Inject
  protected TournamentsRepository tournamentsRepository;
  @Inject
  protected TournamentsState state;
  @Inject
  protected ApplicationState appState;
  @Inject
  private TournamentsStateSynchronizer synchronizer;

  /**
   * deleting a tournament.
   */
  @Transactional
  public void execute() {
    var current = state.getCurrent();

    if (current == null || current.getId() == null) {
      return;
    }

    var currentTournamentId = current.getId();
    log.info("delete with name: '{}' currentTournamentId: '{}'", current.getName(),
      currentTournamentId);

    // reattach current tournament to execute delete via JPA
    tournamentsRepository.delete(current.getId());

    synchronizer.synchronize(state);

    if (Objects.equals(appState.getTournamentId(), currentTournamentId)) {
      appState.setTournament(null);
    }
  }
}
