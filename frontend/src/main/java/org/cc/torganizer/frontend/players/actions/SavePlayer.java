package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class SavePlayer extends PlayersAction {

  @Inject
  private Logger logger;

  @Inject
  private OrderPlayers orderPlayers;

  @Inject
  private CreatePlayer createPlayer;

  /**
   * persisting changes to a already persisted player.
   */
  public void exeute() {
    logger.info("save player");

    Player player = state.getCurrent();

    Long tournamentId = applicationState.getTournamentId();

    if (player.getId() == null) {
      playersRepository.create(player);
      tournamentsRepository.addPlayer(tournamentId, player.getId());
      state.synchronize();
      orderPlayers.execute();
    } else {
      playersRepository.update(player);
    }

    createPlayer.execute();
  }
}
