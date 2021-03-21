package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.squads.SquadsState;
import org.cc.torganizer.frontend.squads.SquadsStateSynchronizer;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.SquadsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Creating a not persisted new Squad.
 */
@RequestScoped
@Named
@SuppressWarnings("unused")
public class CreateSquad extends SquadAction {

  @Inject
  protected SquadsRepository squadsRepository;

  @Inject
  protected PlayersRepository playersRepository;

  @Inject
  protected TournamentsRepository tournamentsRepository;

  @Inject
  private SquadsStateSynchronizer synchronizer;

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

    synchronizer.synchronize(state);
    state.setCurrent(new Squad());
  }
}
