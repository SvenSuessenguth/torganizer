package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.players.PlayersState;

/**
 * Selecting Player.
 */
@RequestScoped
@Named
public class SelectPlayer {

  @Inject
  protected PlayersState state;

  /**
   * selecting a player on UI.
   */
  public String execute(Player player) {
    state.setCurrent(player);

    return "";
  }
}
