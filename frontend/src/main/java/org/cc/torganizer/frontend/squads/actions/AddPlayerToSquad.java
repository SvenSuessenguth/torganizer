package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Player;

/**
 * Adding a player to the current squad.
 */
@RequestScoped
@Named
public class AddPlayerToSquad extends SquadAction {

  /**
   * Functional Interface methode.
   */
  public void execute(Player player) {
    // player must have an id
    var current = state.getCurrent();
    current.addPlayer(player);

    // remove player with no id (if existing)
    for (var p : current.getPlayers()) {
      if (p.getId() == null) {
        current.removePlayer(p);
        break;
      }
    }
  }
}
