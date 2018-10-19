package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;

/**
 * Comparing Opponents by Name.
 */
public class OpponentByNameComparator implements Comparator<Opponent>, Serializable {

 private static final long serialVersionUID = -4391196518411031417L;

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


    List<Player> o1Players = new ArrayList<>(o1.getPlayers());
    List<Player> o2Players = new ArrayList<>(o2.getPlayers());

    if (o1Players.isEmpty() && o2Players.isEmpty()) {
      return 0;
    } else if (o1Players.isEmpty()) {
      return 1;
    } else if (o2Players.isEmpty()) {
      return -1;
    }

    PlayerByNameComparator pbnComparator = new PlayerByNameComparator();
    Collections.sort(o1Players, pbnComparator);
    Collections.sort(o2Players, pbnComparator);

    Player po1 = o1Players.get(0);
    Player p02 = o2Players.get(0);

    return pbnComparator.compare(po1, p02);
  }
}
