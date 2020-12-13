package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

@RequestScoped
@Named
public class CreateSquad extends SquadsAction {

  /**
   * creating a squad an link with current tournament.
   */
  public void execute() {
    Squad current = state.getCurrent();
    Squad newSquad = new Squad();
    squadsRepository.create(newSquad);

    Long tournamentId = applicationState.getTournamentId();
    tournamentsRepository.addOpponent(tournamentId, newSquad.getId());

    for (Player p : current.getPlayers()) {
      Player p2 = playersRepository.read(p.getId());
      newSquad.addPlayer(p2);
    }
    squadsRepository.update(newSquad);

    state.synchronize();
    state.setCurrent(new Squad());
  }
}
