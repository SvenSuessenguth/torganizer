package org.cc.torganizer.frontend.tournaments.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Tournament;

@RequestScoped
@Named
public class SaveTournament extends TournamentsAction {

  @Inject
  private Logger logger;

  @Inject
  private CancelTournament cancelTournament;

  /**
   * save changes on already persisted tournament.
   */
  public void execute() {
    Tournament current = state.getCurrent();

    if (current.getId() != null) {
      tournamentsRepository.update(current);
    } else {
      tournamentsRepository.create(current);
      state.synchronize();
      appState.setTournament(current);
    }

    cancelTournament.execute();

    logger.info("save with name: '{}'", current.getName());
  }
}
