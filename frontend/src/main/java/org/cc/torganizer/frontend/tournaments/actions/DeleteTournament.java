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
    if (current == null || current.getId() == null) {
      return;
    }

    logger.info("delete with name: '{}' id: '{}'", current.getName(), current.getId());

    current = tournamentsRepository.read(current.getId());
    tournamentsRepository.delete(current);
    state.setCurrent(null);
    appState.setTournament(null);
    state.synchronize();
  }
}