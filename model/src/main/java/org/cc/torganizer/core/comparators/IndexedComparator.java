package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.cc.torganizer.core.entities.IIndexed;
import org.cc.torganizer.core.util.Checker;

/**
 * <p>
 * IndexedComparator class.
 * </p>
 * 
 * @author svens
 * @version $Id: $
 */
public class IndexedComparator
  implements Comparator<IIndexed>, Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = 4376943538050087469L;

  /**
   * Default.
   */
  public IndexedComparator() {
    // gem. Bean-Spec.
  }

  /** {@inheritDoc} */
  @Override
  public int compare(IIndexed i0, IIndexed i1) {

    // beide NULL = 0
    if (new Checker().allIsNull(i0, i1)) {
      return 0;
    }

    // genau einer ist NULL
    if (new Checker().onlyOneIsNull(i0, i1)) {
      return i0 == null ? 1 : -1;
    }

    // keiner ist NULL
    return i0.getIndex().compareTo(i1.getIndex());
  }
}
