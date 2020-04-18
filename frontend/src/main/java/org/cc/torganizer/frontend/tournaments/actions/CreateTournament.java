package org.cc.torganizer.frontend.tournaments.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Tournament;

@RequestScoped
@Named
public class CreateTournament extends TournamentsAction {

  @Inject
  private Logger logger;

  /**
   * creating and persist a new tournament.
   */
  public void execute() {
    logger.info("create with name: '{}'", state.getCurrentName());

    Tournament newTournament = new Tournament();
    newTournament.setName(state.getCurrentName());

    newTournament = tournamentsRepository.create(newTournament);

    state.synchronize();
    state.setCurrent(newTournament);
    appState.setTournament(newTournament);
  }
}
