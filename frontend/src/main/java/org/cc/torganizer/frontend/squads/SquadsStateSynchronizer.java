package org.cc.torganizer.frontend.squads;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Synchronizing the tournaments state with db-data.
 */
@RequestScoped
public class SquadsStateSynchronizer {

  public static final int MAX_SQUADS_RESULTS = 1000;
  public static final int MAX_PLAYERS_RESULTS = 1000;

  public static final int CURRENT_SQUAD_PLAYERS_TABLE_SIZE = 2;

  @Inject
  private ApplicationState applicationState;
  @Inject
  private TournamentsRepository tournamentsRepository;

  /**
   * Synchronizing the tournaments state with db-data.
   */
  public void synchronize(SquadsState state) {
    var currentTournament = applicationState.getTournament();
    var tournamentId = currentTournament.getId();

    state.setSquads(tournamentsRepository.getSquads(tournamentId, 0, MAX_SQUADS_RESULTS));
    state.setPlayers(tournamentsRepository.getPlayers(tournamentId, 0, MAX_PLAYERS_RESULTS));

    // to show table, add empty players with no id, which are replaced
    // when actual players are added
    state.setCurrent(new Squad());
    for (var i = 0; i < CURRENT_SQUAD_PLAYERS_TABLE_SIZE; i++) {
      state.getCurrent().addPlayer(new Player(new Person()));
    }
  }
}
