package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.players.PlayersState;
import org.cc.torganizer.frontend.players.PlayersStateSynchronizer;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Persisting selected Player.
 */
@RequestScoped
@Named
public class SavePlayer {

  @Inject
  private Logger logger;

  @Inject
  protected PlayersState state;

  @Inject
  protected PlayersRepository playersRepository;

  @Inject
  protected ApplicationState applicationState;

  @Inject
  protected TournamentsRepository tournamentsRepository;

  @Inject
  private OrderPlayers orderPlayers;

  @Inject
  private CancelPlayer createPlayer;

  @Inject
  private PlayersStateSynchronizer synchronizer;

  /**
   * persisting changes to a already persisted player.
   */
  public void execute() {
    logger.info("save player");

    Player player = state.getCurrent();
    Long tournamentId = applicationState.getTournamentId();

    if (player.getId() == null) {
      playersRepository.create(player);
      tournamentsRepository.addOpponent(tournamentId, player.getId());
      synchronizer.synchronize(state);
    } else {
      playersRepository.update(player);
    }

    orderPlayers.execute();
    createPlayer.execute();
  }
}
