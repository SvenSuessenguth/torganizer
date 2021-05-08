package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Persisting selected Player.
 */
@RequestScoped
@Named
public class SavePlayer extends PlayersAction {

  @Inject
  private Logger logger;

  @Inject
  protected TournamentsRepository tournamentsRepository;

  @Inject
  private OrderPlayers orderPlayers;

  @Inject
  private CancelPlayer createPlayer;

  /**
   * persisting changes to a already persisted player.
   */
  public void execute() {
    logger.info("save player");

    var player = state.getCurrent();
    Long tournamentId = applicationState.getTournamentId();

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
