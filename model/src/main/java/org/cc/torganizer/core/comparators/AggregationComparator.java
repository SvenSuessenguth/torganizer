package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.cc.torganizer.core.entities.aggregates.Aggregation;
import org.cc.torganizer.core.util.Checker;

/**
 * Vergleich von aggregierten Daten (gewonnene/verlorene Matches, Saetze,
 * Punkte) um eine Tabelle aufbauen zu koennen.
 * 
 * @author svens
 */
public class AggregationComparator
  implements Comparator<Aggregation>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -5450778037135481288L;

  /**
   * Default.
   */
  public AggregationComparator() {
    // gem. Bean-Spec.
  }

  @Override
  public int compare(Aggregation o1, Aggregation o2) {
    assert o1 != null && o2 != null;

    int result = 0;

    // matches > results > scores
    if (Checker.isNoNullValue(o1.getMa(), o2.getMa()) && !o1.getMa().equals(o2.getMa())) {
      result = o1.getMa().getRatio().compareTo(o2.getMa().getRatio());
    } else if (Checker.isNoNullValue(o1.getRa(), o2.getRa()) && !o1.getRa().equals(o2.getRa())) {
      result = o1.getRa().getRatio().compareTo(o2.getRa().getRatio());
    } else if (Checker.isNoNullValue(o1.getSa(), o2.getSa()) && !o1.getSa().equals(o2.getSa())) {
      result = o1.getSa().getRatio().compareTo(o2.getSa().getRatio());
    }
    return result;
  }
}