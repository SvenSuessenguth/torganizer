package org.cc.torganizer.core.comparators.player;

import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@RequestScoped
public class PlayerComparatorProvider {

  @Inject
  private Instance<PlayerComparator> playerComparators;

  public PlayerComparator get(PlayerOrder playerOrder) {

    for (PlayerComparator pc : playerComparators) {
      if (Objects.equals(playerOrder, pc.getPlayerOrder())) {
        return pc;
      }
    }

    return null;
  }
}
