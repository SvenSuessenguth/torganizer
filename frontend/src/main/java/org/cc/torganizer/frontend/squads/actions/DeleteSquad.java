package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
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
public class DeleteSquad extends SquadAction {

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
    var tournamentId = applicationState.getTournamentId();
    var squad = state.getCurrent();

    if (tournamentId == null || squad == null) {
      return;
    }

    var squadId = squad.getId();
    tournamentsRepository.removeOpponent(tournamentId, squadId);
    squadsRepository.delete(squadId);
    synchronizer.synchronize(state);
  }
}
