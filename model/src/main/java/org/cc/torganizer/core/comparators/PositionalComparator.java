package org.cc.torganizer.core.comparators;

import static org.cc.torganizer.core.util.Checker.countNullValues;
import static org.cc.torganizer.core.util.Checker.onlyNullValues;

import java.io.Serializable;
import java.util.Comparator;
import org.cc.torganizer.core.entities.Positional;

/**
 * Sortieren nach der Position.
 */
public class PositionalComparator implements Comparator<Positional>, Serializable {

  @Override
  public final int compare(final Positional p0, final Positional p1) {

    // both are NULL
    if (onlyNullValues(p0, p1)) {
      return 0;
    }

    // exact one value is NULL
    if (countNullValues(p0, p1) == 1) {
      int result;
      if (p0 == null) {
        result = 1;
      } else {
        result = -1;
      }

      return result;
    }

    // none is NULL
    return p0.getPosition().compareTo(p1.getPosition());
  }
}
