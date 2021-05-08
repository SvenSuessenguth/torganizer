package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.squads.SquadsStateSynchronizer;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.SquadsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Saving a squad.
 */
@RequestScoped
@Named
@SuppressWarnings("unused")
public class SaveSquad extends SquadAction {

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
    var squad = state.getCurrent();
    Long tournamentId = applicationState.getTournamentId();

    if (squad.getId() == null) {
      squadsRepository.create(squad);
      tournamentsRepository.addOpponent(tournamentId, squad.getId());
    } else {
      squadsRepository.update(squad);
    }

    synchronizer.synchronize(state);
  }
}
