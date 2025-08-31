package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Persisting selected Player.
 */
@RequestScoped
@Named
@Slf4j
public class SavePlayer extends PlayersAction {
  @Inject
  protected TournamentsRepository tournamentsRepository;
  @Inject
  private OrderPlayers orderPlayers;
  @Inject
  private CancelPlayer createPlayer;

  /**
   * persisting changes to an already persisted player.
   */
  public void execute() {
    log.info("save player");

    var player = state.getCurrent();
    var tournamentId = applicationState.getTournamentId();

    if (player.getId() == null) {
      repository.create(player);
      tournamentsRepository.addOpponent(tournamentId, player.getId());
      synchronizer.synchronize(state);
    } else {
      repository.update(player);
    }

    orderPlayers.execute();
    createPlayer.execute();
  }
}
