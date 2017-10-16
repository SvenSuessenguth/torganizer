package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.cc.torganizer.core.entities.Opponent;

/**
 * Sortierung nach dem Vereinsnamen des ersten Players (Weil ein Opponent auch
 * ein Squad oder Team sein kann). Wenn kein Verein zugewiesen ist, dann ist der
 * Opponent immer 'kleiner'.
 * 
 * @author svens
 * @version $Id: $
 */
public class OpponentByClubComparator
  implements Comparator<Opponent>, Serializable {

  /** serialVersionUID . */
  private static final long serialVersionUID = 3462304861691282602L;

  /**
   * Default.
   */
  public OpponentByClubComparator() {
    // gem. Bean-Spec.
  }

  /** {@inheritDoc} */
  @Override
  public int compare(Opponent o1, Opponent o2) {

    // Bye hat keinen Player!
//    Player p1 = o1.getPlayers().size() > 0 ? o1.getPlayers().get(0) : null;
//    Player p2 = o2.getPlayers().size() > 0 ? o2.getPlayers().get(0) : null;
//    Club c1 = p1 == null ? null : p1.getClub();
//    Club c2 = p2 == null ? null : p2.getClub();
//    String s1 = c1 == null ? "" : c1.getName();
//    String s2 = c2 == null ? "" : c2.getName();
//    return s1.compareToIgnoreCase(s2);
    return 0;
  }
}
