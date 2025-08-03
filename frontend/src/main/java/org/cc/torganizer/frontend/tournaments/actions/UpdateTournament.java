package org.cc.torganizer.frontend.tournaments.actions;

import static jakarta.faces.application.FacesMessage.SEVERITY_ERROR;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.tournaments.TournamentsBacking;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.persistence.TournamentsRepository;
import org.slf4j.Logger;

/**
 * Saving the current tournament.
 */
@RequestScoped
@Named
public class UpdateTournament {

  @Inject
  private Logger logger;
  @Inject
  protected TournamentsRepository tournamentsRepository;
  @Inject
  protected TournamentsState state;
  @Inject
  private CancelTournament cancelTournament;
  @Inject
  private TournamentsBacking tournamentsBacking;
  @Inject
  private FacesContext facesContext;

  /**
   * save changes on already persisted tournament.
   */
  public void execute() {
    var current = state.getCurrent();

    try {
      tournamentsRepository.update(current);
    } catch (Exception e) {
      var facesMessage = new FacesMessage(SEVERITY_ERROR,
          "Error saving tournament '%s'".formatted(current.getName()), e.getMessage());
      facesContext.addMessage(tournamentsBacking.getNameClientId(), facesMessage);
      tournamentsBacking.getNameInputText().setValid(false);

      return;
    }

    cancelTournament.execute();
    logger.info("update tournament '{}'", current.getName());
  }
}
