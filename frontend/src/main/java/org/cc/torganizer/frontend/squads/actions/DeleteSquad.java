package org.cc.torganizer.frontend.squads.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Squad;

/**
 * Deleting a squad.
 */
@RequestScoped
@Named
public class DeleteSquad extends SquadsAction {

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
    state.synchronize();
  }
}
