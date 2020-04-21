package org.cc.torganizer.frontend.squads.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

@RequestScoped
@Named
public class SaveSquad extends SquadsAction {

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
