package org.cc.torganizer.core.comparators.player;

import org.cc.torganizer.core.entities.Player;

import javax.enterprise.context.RequestScoped;
import java.util.Objects;

import static org.cc.torganizer.core.comparators.player.PlayerOrder.BY_LAST_NAME;

/**
 * Comparing players by last name.
 */
@RequestScoped
public class PlayerByLastNameComparator implements PlayerComparator {

  private static final long serialVersionUID = 6249336100950968432L;

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

    String o1LastName = o1.getPerson().getLastName();
    String o2LastName = o2.getPerson().getLastName();

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
  public PlayerOrder getPlayerOrder() {
    return BY_LAST_NAME;
  }
}
