package org.cc.torganizer.frontend.squads.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class RemovePlayerFromSquad extends SquadsAction {
  public void execute(Player player) {
    state.getCurrent().getPlayers().remove(player);
  }
}
