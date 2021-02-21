package org.cc.torganizer.frontend.tournaments.actions;

import static jakarta.faces.application.FacesMessage.SEVERITY_ERROR;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.tournaments.TournamentsBacking;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.frontend.tournaments.TournamentsStateSynchronizer;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Saving the current Tournament.
 */
@RequestScoped
@Named
public class SaveTournament {

  @Inject
  private Logger logger;

  @Inject
  protected TournamentsRepository tournamentsRepository;

  @Inject
  protected TournamentsState state;

  @Inject
  protected ApplicationState appState;

  @Inject
  private CancelTournament cancelTournament;

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

    // can't save two tournaments with same name
    for (Tournament t : state.getTournaments()) {
      if (Objects.equals(t.getName(), current.getName())
          && !Objects.equals(t.getId(), current.getId())) {
        FacesMessage facesMessage = new FacesMessage(SEVERITY_ERROR,
            "Zwei Turniere mit dem selben Namen", "Fehler");
        facesContext.addMessage(tournamentsBacking.getNameClientId(), facesMessage);
        tournamentsBacking.getNameInputText().setValid(false);

        return;
      }
    }

    if (current.getId() != null) {
      tournamentsRepository.update(current);
    } else {
      tournamentsRepository.create(current);
      synchronizer.synchronize(state);
      appState.setTournament(current);
    }

    cancelTournament.execute();

    logger.info("save with name: '{}'", current.getName());
  }
}
