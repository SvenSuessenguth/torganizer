package org.cc.torganizer.frontend.squads.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Squad;

/**
 * Saving a squad.
 */
@RequestScoped
@Named
public class SaveSquad extends SquadsAction {

  /**
   * Functional Interface Methode.
   */
  public void execute() {

    Squad squad = state.getCurrent();
    Long tournamentId = applicationState.getTournamentId();

    if (squad.getId() == null) {
      squadsRepository.create(squad);
      tournamentsRepository.addOpponent(tournamentId, squad.getId());
    } else {
      squadsRepository.update(squad);
    }

    state.synchronize();
  }
}
