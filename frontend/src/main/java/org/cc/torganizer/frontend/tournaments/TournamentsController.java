package org.cc.torganizer.frontend.tournaments;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.logging.SimplifiedLoggerFacade;
import org.cc.torganizer.frontend.logging.online.Online;
import org.cc.torganizer.persistence.TournamentsRepository;

@Named
@RequestScoped
public class TournamentsController {

  @Inject
  @Online
  private SimplifiedLoggerFacade logger;

  @Inject
  private TournamentsState state;

  @Inject
  private TournamentsRepository tournamentsRepository;

  public String getCurrentName() {
    return state.getCurrent().getName();
  }

  public void setCurrentName(String currentName) {
    state.getCurrent().setName(currentName);
  }

  /**
   * save the tournaments new name.
   */
  public void saveTournament() {
    logger.severe("save selected with name: '" + state.getCurrent().getName() + "'");

    tournamentsRepository.update(state.getCurrent());
    state.initState();
  }

  /**
   * creating and persist a new tournament.
   */
  public void newTournament() {
    logger.severe("new with name: '" + state.getCurrent().getName() + "'");

    Tournament newTournament = new Tournament();
    newTournament.setName("Tournament");

    tournamentsRepository.create(newTournament);
    state.setCurrent(newTournament);
    state.initState();
  }

  public void deleteTournament() {

  }
}
