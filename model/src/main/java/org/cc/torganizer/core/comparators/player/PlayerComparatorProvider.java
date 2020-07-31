package org.cc.torganizer.core.comparators.player;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Objects;

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
