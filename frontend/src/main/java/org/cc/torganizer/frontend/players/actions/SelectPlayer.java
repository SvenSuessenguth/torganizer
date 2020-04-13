package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class SelectPlayer extends PlayersAction {

  public void execute(Player player) {
    state.setCurrent(player);
    Club club = player.getClub();
    if (club != null) {
      state.setCurrentClubId(club.getId());
    }
  }
}
