package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class SelectPlayer extends PlayersAction {

  /**
   * selecting a player on UI.
   */
  public String execute(Player player) {
    state.setCurrent(player);

    return null;
  }
}
