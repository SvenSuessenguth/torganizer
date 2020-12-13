package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import org.cc.torganizer.core.comparators.player.PlayerComparator;
import org.cc.torganizer.core.comparators.player.PlayerComparatorProvider;
import org.cc.torganizer.core.comparators.player.PlayerOrderCriteria;
import org.cc.torganizer.core.entities.Player;

/**
 * Ordering Players by selected Criteria.
 */
@RequestScoped
@Named
public class OrderPlayers extends PlayersAction {

  @Inject
  private PlayerComparatorProvider playerComparatorProvider;

  @SuppressWarnings("unused")
  public void execute(AjaxBehaviorEvent event) {
    execute();
  }

  /**
   * ordering players with given order rule.
   */
  public void execute() {
    PlayerOrderCriteria playerOrderCriteria = state.getPlayerOrderCriteria();
    List<Player> players = state.getPlayers();

    PlayerComparator comparator = playerComparatorProvider.get(playerOrderCriteria);
    players.sort(comparator);
  }
}
