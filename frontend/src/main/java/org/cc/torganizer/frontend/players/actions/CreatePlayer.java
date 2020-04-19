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
    Player player = new Player(new Person());
    state.setCurrent(player);
  }
}
