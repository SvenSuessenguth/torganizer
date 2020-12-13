package org.cc.torganizer.core.comparators.player;

import java.util.Objects;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@RequestScoped
public class PlayerComparatorProvider {

  @Inject
  private Instance<PlayerComparator> playerComparators;

  /**
   * providing the comparartor by the given playerOrder.
   */
  public PlayerComparator get(PlayerOrder playerOrder) {

    for (PlayerComparator pc : playerComparators) {
      if (Objects.equals(playerOrder, pc.getPlayerOrder())) {
        return pc;
      }
    }

    return null;
  }
}
