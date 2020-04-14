package org.cc.torganizer.frontend.players.actions;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.comparators.player.PlayerComparator;
import org.cc.torganizer.core.comparators.player.PlayerComparatorProvider;
import org.cc.torganizer.core.comparators.player.PlayerOrder;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class OrderPlayers extends PlayersAction {

  @Inject
  private PlayerComparatorProvider playerComparatorProvider;

  @SuppressWarnings("unused")
  public void execute(AjaxBehaviorEvent event) {
    execute();
  }

  public void execute() {
    PlayerOrder playerOrder = state.getPlayerOrder();
    List<Player> players = state.getPlayers();

    PlayerComparator comparator = playerComparatorProvider.get(playerOrder);
    players.sort(comparator);
  }
}
