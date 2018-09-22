package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import org.cc.torganizer.core.entities.Player;

/**
 * Comparing players by last name.
 */
public class PlayerByNameComparator implements Comparator<Player>, Serializable {

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

    String o1LastName = o1.getPerson().getLastName().toLowerCase();
    String o2LastName = o2.getPerson().getLastName().toLowerCase();

    return o1LastName.compareTo(o2LastName);
  }
}
