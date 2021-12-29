package org.cc.torganizer.frontend.tournaments.actions;

import static jakarta.faces.application.FacesMessage.SEVERITY_ERROR;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.tournaments.TournamentsBacking;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.frontend.tournaments.TournamentsStateSynchronizer;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Saving the current tournament.
 */
@RequestScoped
@Named
public class CreateTournament {

  @Inject
  private Logger logger;

  @Inject
  protected TournamentsRepository tournamentsRepository;

  @Inject
  protected TournamentsState state;

  @Inject
  protected ApplicationState appState;

  @Inject
  private TournamentsBacking tournamentsBacking;

  @Inject
  private TournamentsStateSynchronizer synchronizer;

  @Inject
  private FacesContext facesContext;

  /**
   * save changes on already persisted tournament.
   */
  public void execute() {
    Tournament current = state.getCurrent();

    try {
      tournamentsRepository.create(current);
      synchronizer.synchronize(state);
      appState.setTournament(current);

      logger.info("save with name: '{}'", current.getName());
    } catch (Exception e) {
      var facesMessage = new FacesMessage(SEVERITY_ERROR,
          "Error saving tournament '%s'".formatted(current.getName()), e.getMessage());
      facesContext.addMessage(tournamentsBacking.getNameClientId(), facesMessage);
      tournamentsBacking.getNameInputText().setValid(false);
      current.setId(null);
    }
  }
}
