package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

/**
 * Adding a player to the current squad.
 */
@RequestScoped
@Named
@SuppressWarnings("unused")
public class AddPlayerToSquad extends SquadAction {

  /**
   * Functional Interface methode.
   */
  public void execute(Player player) {
    // player must have an id
    Squad current = state.getCurrent();
    current.addPlayer(player);

    // remove player with no id (if existing)
    for (Player p : current.getPlayers()) {
      if (p.getId() == null) {
        current.removePlayer(p);
        break;
      }
    }
  }
}
