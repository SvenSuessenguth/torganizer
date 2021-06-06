package org.cc.torganizer.core.comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.cc.torganizer.core.comparators.player.PlayerByLastNameComparator;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;

/**
 * Comparing Opponents by Name.
 */
public class OpponentByNameComparator implements Comparator<Opponent>{

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

    var pbnComparator = new PlayerByLastNameComparator();


    o1Players.sort(pbnComparator);
    o2Players.sort(pbnComparator);

    var po1 = o1Players.get(0);
    var p02 = o2Players.get(0);

    return pbnComparator.compare(po1, p02);
  }
}
