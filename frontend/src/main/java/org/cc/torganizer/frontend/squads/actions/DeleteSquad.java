package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.squads.SquadsState;
import org.cc.torganizer.frontend.squads.SquadsStateSynchronizer;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.SquadsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Deleting a squad.
 */
@RequestScoped
@Named
@SuppressWarnings("unused")
public class DeleteSquad {

  @Inject
  protected SquadsState state;

  @Inject
  protected ApplicationState applicationState;

  @Inject
  protected SquadsRepository squadsRepository;

  @Inject
  protected PlayersRepository playersRepository;

  @Inject
  protected TournamentsRepository tournamentsRepository;

  @Inject
  private SquadsStateSynchronizer synchronizer;

  /**
   * Functional Interface Methode.
   */
  public void execute() {
    // delete from tournament and from squads-tables
    Long tournamentId = applicationState.getTournamentId();
    Squad squad = state.getCurrent();

    if (tournamentId == null || squad == null) {
      return;
    }

    Long squadId = squad.getId();
    tournamentsRepository.removeOpponent(tournamentId, squadId);
    squadsRepository.delete(squadId);
    synchronizer.synchronize(state);
  }
}
