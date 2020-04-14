package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class SavePlayer extends PlayersAction {

  @Inject
  private Logger logger;

  @Inject
  private OrderPlayers orderPlayers;

  public void exeute() {
    logger.info("save player");
    Long clubId = state.getCurrentClubId();
    Club club = clubsRepository.read(clubId);

    Player current = state.getCurrent();
    current.setClub(club);
    Player updated = playersRepository.update(current);
    state.setCurrent(updated);
    orderPlayers.execute();
  }
}
