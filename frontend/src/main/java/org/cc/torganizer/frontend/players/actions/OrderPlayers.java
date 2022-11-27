package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.comparators.player.PlayerComparatorProvider;

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
    var playerOrderCriteria = state.getPlayerOrderCriteria();
    var players = state.getPlayers();

    var comparator = playerComparatorProvider.get(playerOrderCriteria);
    players.sort(comparator);
  }
}
