package org.cc.torganizer.core.comparators.player;

import static org.cc.torganizer.core.comparators.player.PlayerOrderCriteria.BY_LAST_NAME;

import jakarta.enterprise.context.RequestScoped;
import java.util.Objects;
import org.cc.torganizer.core.entities.Player;

/**
 * Comparing players by last name.
 */
@RequestScoped
public class PlayerByLastNameComparator implements PlayerComparator {

  @Override
  public final int compare(final Player o1, final Player o2) {
    if (Objects.equals(o1, o2)) {
      return 0;
    }

    if (o1 == null) {
      return -1;
    } else if (o2 == null) {
      return 1;
    }

    var o1LastName = o1.getPerson().getLastName();
    var o2LastName = o2.getPerson().getLastName();

    if (o1LastName == null && o2LastName == null) {
      return 0;
    } else if (o1LastName == null) {
      return -1;
    } else if (o2LastName == null) {
      return 1;
    }

    return o1LastName.toLowerCase().compareTo(o2LastName.toLowerCase());
  }

  @Override
  public PlayerOrderCriteria getPlayerOrderCriteria() {
    return BY_LAST_NAME;
  }
}
