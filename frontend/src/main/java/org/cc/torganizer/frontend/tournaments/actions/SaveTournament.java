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

  /**
   * save changes on already persisted tournament.
   */
  public void execute() {
    Tournament current = state.getCurrent();
    current.setName(state.getCurrentName());

    Tournament updated = tournamentsRepository.update(current);
    state.setCurrent(updated);
    state.synchronize();

    logger.info("save with name: '{}'", current.getName());
  }
}
