package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

/**
 * Cancel editing a Player.
 */
@RequestScoped
@Named
public class CancelPlayer extends PlayersAction {

  /**
   * creating a player without persisting it.
   */
  public String execute() {
    Player player = new Player(new Person());
    state.setCurrent(player);

    return null;
  }
}
