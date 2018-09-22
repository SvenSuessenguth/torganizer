package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.cc.torganizer.core.entities.Match;

/**
 * Erm\u00F6glichen einer Sortierung, die fuer eine gleichmaessige Wartezeit der
 * Spieler sorgt.
 */
public class MatchHistoryComparator implements Comparator<Match>, Serializable {

  /**
   * serialVersionUID .
   */
  private static final long serialVersionUID = 4833758925262696791L;

  /**
   * Default.
   */
  public MatchHistoryComparator() {
    // gem. Bean-Spec.
  }

  @Override
  public final int compare(final Match m1, final Match m2) {

    // durchschnittliche Wartezeit der Player ermitteln
    Long idleTimeMatch1 = m1.getIdleTime();
    Long idleTimeMatch2 = m2.getIdleTime();

    return idleTimeMatch2.compareTo(idleTimeMatch1);
  }
}
