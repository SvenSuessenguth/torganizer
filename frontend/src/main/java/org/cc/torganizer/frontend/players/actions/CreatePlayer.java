package org.cc.torganizer.frontend.players.actions;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class CreatePlayer extends PlayersAction {

  /**
   * creating a player without persisting it.
   */
  public void execute() {
    List<Club> clubs = clubsRepository.read(0, 1);

    Player player = new Player(new Person());
    player.setClub(clubs.get(0));
    state.setCurrent(player);
  }
}
