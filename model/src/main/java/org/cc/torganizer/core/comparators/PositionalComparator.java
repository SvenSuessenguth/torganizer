package org.cc.torganizer.core.comparators;

import java.util.Comparator;

import org.cc.torganizer.core.entities.IPositional;
import org.cc.torganizer.core.util.Checker;

/**
 * Sortieren nach der Position.
 */
public class PositionalComparator implements Comparator<IPositional> {

  @Override
  public final int compare(final IPositional p0, final IPositional p1) {

    // beide NULL = 0
    if (new Checker().allIsNull(p0, p1)) {
      return 0;
    }

    // genau einer ist NULL
    if (new Checker().onlyOneIsNull(p0, p1)) {
      int result = 0;
      if (p0 == null) {
        result = 1;
      } else {
        result = -1;
      }

      return result;
    }

    // keiner ist NULL
    return p0.getPosition().compareTo(p1.getPosition());
  }
}
