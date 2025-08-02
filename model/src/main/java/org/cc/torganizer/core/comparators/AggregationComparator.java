package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.Comparator;
import org.cc.torganizer.core.entities.aggregates.Aggregation;
import org.cc.torganizer.core.util.Checker;

/**
 * Vergleich von aggregierten Daten (gewonnene/verlorene Matches, Saetze,
 * Punkte) um eine Tabelle aufbauen zu koennen.
 */
public class AggregationComparator implements Comparator<Aggregation>, Serializable {

  @Override
  public final int compare(final Aggregation o1, final Aggregation o2) {
    if (o1 == null || o2 == null) {
      throw new IllegalArgumentException("compare only non null values");
    }

    var result = 0;

    // matches > results > scores
    if (Checker.isNoNullValue(o1.getMatchAggregate(), o2.getMatchAggregate()) && !o1.getMatchAggregate().equals(o2.getMatchAggregate())) {
      result = o1.getMatchAggregate().getRatio().compareTo(o2.getMatchAggregate().getRatio());
    } else if (Checker.isNoNullValue(o1.getResultAggregate(), o2.getResultAggregate()) && !o1.getResultAggregate().equals(o2.getResultAggregate())) {
      result = o1.getResultAggregate().getRatio().compareTo(o2.getResultAggregate().getRatio());
    } else if (Checker.isNoNullValue(o1.getScoreAggregate(), o2.getScoreAggregate()) && !o1.getScoreAggregate().equals(o2.getScoreAggregate())) {
      result = o1.getScoreAggregate().getRatio().compareTo(o2.getScoreAggregate().getRatio());
    }
    return result;
  }
}
