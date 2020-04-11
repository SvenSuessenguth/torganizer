package org.cc.torganizer.frontend.players;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.comparators.player.PlayerComparator;
import org.cc.torganizer.core.comparators.player.PlayerComparatorProvider;
import org.cc.torganizer.core.comparators.player.PlayerOrder;
import org.cc.torganizer.core.entities.Player;

@Named
@RequestScoped
public class PlayersService {

  @Inject
  private PlayersState playersState;

  @Inject
  PlayerComparatorProvider playerComparatorProvider;

  public void orderPlayers() {
    PlayerOrder playerOrder = playersState.getPlayerOrder();
    List<Player> players = playersState.getPlayers();

    PlayerComparator comparator = playerComparatorProvider.get(playerOrder);
    players.sort(comparator);
  }

}
