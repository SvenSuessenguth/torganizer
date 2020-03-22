package org.cc.torganizer.frontend.players;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.Numbers;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.PlayersRepository;

@Named
@RequestScoped
public class PlayersController {

  @Inject
  private Logger logger;

  @Inject
  private PlayersRepository playersRepository;

  @Inject
  private ClubsRepository clubsRepository;

  @Inject
  private PlayersState playersState;

  public void save() {
    logger.info("save player");
    Long clubId = new Numbers().getLong(playersState.getCurrentClubId());
    Club club = clubsRepository.read(clubId);

    Player current = playersState.getCurrent();
    current.setClub(club);
    Player update = playersRepository.update(current);
    playersState.setCurrent(update);
  }

  public void create() {
    logger.info("create player");
  }

  public void delete() {
    logger.info("delete player");
  }

  public void select(Player player) {
    playersState.setCurrent(player);
  }
}
