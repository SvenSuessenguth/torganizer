package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import org.cc.torganizer.core.comparators.player.PlayerByLastNameComparator;
import org.cc.torganizer.core.entities.Opponent;

/**
 * Comparing Opponents by Name.
 */
public class OpponentByNameComparator implements Comparator<Opponent>, Serializable {

  @Override
  public final int compare(final Opponent o1, final Opponent o2) {

    if (Objects.equals(o1, o2)) {
      return 0;
    }
    if (o1 == null) {
      return 1;
    } else if (o2 == null) {
      return -1;
    }

    var o1Players = new ArrayList<>(o1.getPlayers());
    var o2Players = new ArrayList<>(o2.getPlayers());

    if (o1Players.isEmpty() && o2Players.isEmpty()) {
      return 0;
    } else if (o1Players.isEmpty()) {
      return 1;
    } else if (o2Players.isEmpty()) {
      return -1;
    }

    var pbnComparator = new PlayerByLastNameComparator();


    o1Players.sort(pbnComparator);
    o2Players.sort(pbnComparator);

    var po1 = o1Players.getFirst();
    var p02 = o2Players.getFirst();

    return pbnComparator.compare(po1, p02);
  }
}
