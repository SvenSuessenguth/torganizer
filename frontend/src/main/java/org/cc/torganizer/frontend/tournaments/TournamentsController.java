package org.cc.torganizer.frontend.tournaments;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
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

  @Inject
  private FacesContext facesContext;

  private UIComponent uiCurrentName;

  public String getCurrentName() {
    Tournament current = state.getCurrent();
    return current == null ? "" : current.getName();
  }

  public void setCurrentName(String currentsNewName) {
    state.setCurrentsNewName(currentsNewName);
  }

  /**
   * save the tournaments new name.
   */
  public void save() {
    Tournament current = state.getCurrent();
    current.setName(state.getCurrentsNewName());

    tournamentsRepository.update(current);
    state.initState();

    logger.info("save selected with name: '{}'", current.getName());
  }

  /**
   * creating and persist a new tournament.
   */
  public void create() {
    logger.info("create with name: '{}'", state.getCurrentsNewName());

    Tournament newTournament = new Tournament();
    newTournament.setName(state.getCurrentsNewName());

    newTournament = tournamentsRepository.create(newTournament);
    state.setCurrent(newTournament);
    state.initState();
  }


  @Transactional
  public void delete() {
    Tournament current = state.getCurrent();
    if (current == null) {
      return;
    }

    logger.info("delete with name: '{}' id: '{}'", current.getName(), current.getId());

    current = tournamentsRepository.read(current.getId());
    tournamentsRepository.delete(current);
    state.setCurrent(null);
    state.initState();
  }

  public UIComponent getUiCurrentName() {
    return uiCurrentName;
  }

  public void setUiCurrentName(UIComponent uiCurrentName) {
    this.uiCurrentName = uiCurrentName;
  }

}
