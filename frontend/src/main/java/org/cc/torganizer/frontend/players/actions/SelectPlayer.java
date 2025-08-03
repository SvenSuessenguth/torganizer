package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Player;

/**
 * Selecting Player.
 */
@RequestScoped
@Named
public class SelectPlayer extends PlayersAction {

  public String execute(Player player) {
    state.setCurrent(player);

    return "";
  }
}
