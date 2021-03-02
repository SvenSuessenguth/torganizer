package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.players.PlayersState;

/**
 * Cancel editing a Player.
 */
@RequestScoped
@Named
public class CancelPlayer {

  @Inject
  private PlayersState state;

  /**
   * creating a player without persisting it.
   */
  public String execute() {
    Player player = new Player(new Person());
    state.setCurrent(player);

    return null;
  }
}
