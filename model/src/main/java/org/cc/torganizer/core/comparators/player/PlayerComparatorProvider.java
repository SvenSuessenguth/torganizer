package org.cc.torganizer.core.comparators.player;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.Objects;

/**
 * Providing comkparators for Players.
 */
@RequestScoped
public class PlayerComparatorProvider {

  @Inject
  private Instance<PlayerComparator> playerComparators;

  /**
   * providing the comparartor by the given playerOrderCriteria.
   */
  public PlayerComparator get(PlayerOrderCriteria playerOrderCriteria) {

    for (var pc : playerComparators) {
      if (Objects.equals(playerOrderCriteria, pc.getPlayerOrderCriteria())) {
        return pc;
      }
    }

    return null;
  }
}
