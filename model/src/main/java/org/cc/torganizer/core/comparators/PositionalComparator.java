package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.IPositional;

import java.io.Serializable;
import java.util.Comparator;

import static org.cc.torganizer.core.util.Checker.countNullValues;
import static org.cc.torganizer.core.util.Checker.onlyNullValues;

/**
 * Sortieren nach der Position.
 */
public class PositionalComparator implements Comparator<IPositional>, Serializable {

  @Override
  public final int compare(final IPositional p0, final IPositional p1) {

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
