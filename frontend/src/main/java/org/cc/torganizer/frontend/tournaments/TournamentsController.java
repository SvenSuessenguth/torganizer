package org.cc.torganizer.frontend.tournaments;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.persistence.TournamentsRepository;

@Named
@RequestScoped
public class TournamentsController {

  @Inject
  private Logger logger;

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
  public void save() {
    logger.info("save selected with name: '{}'", state.getCurrent().getName());

    tournamentsRepository.update(state.getCurrent());
    state.initState();
  }

  /**
   * creating and persist a new tournament.
   */
  public void create() {
    logger.info("new with name: '{}'", state.getCurrent().getName());

    Tournament newTournament = new Tournament();
    newTournament.setName("Tournament");

    tournamentsRepository.create(newTournament);
    state.setCurrent(newTournament);
    state.initState();
  }
}
