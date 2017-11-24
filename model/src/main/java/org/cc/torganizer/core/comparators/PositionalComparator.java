package org.cc.torganizer.core.comparators;

import java.util.Comparator;

import org.cc.torganizer.core.entities.IPositional;
import org.cc.torganizer.core.util.Checker;

/**
 * Sortieren nach der Position.
 */
public class PositionalComparator
  implements Comparator<IPositional>{

  /**
   * Default.
   */
  public PositionalComparator() {
    // gem. Bean-Spec.
  }

  /** {@inheritDoc} */
  @Override
  public int compare(IPositional p0, IPositional p1) {

    // beide NULL = 0
    if (new Checker().allIsNull(p0, p1)) {
      return 0;
    }

    // genau einer ist NULL
    if (new Checker().onlyOneIsNull(p0, p1)) {
      return p0 == null ? 1 : -1;
    }

    // keiner ist NULL
    return p0.getPosition().compareTo(p1.getPosition());
  }
}
