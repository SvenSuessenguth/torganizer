package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import org.cc.torganizer.core.entities.Club;

public class ClubComparator implements Comparator<Club>, Serializable {

  @Override
  public int compare(Club o1, Club o2) {

    if (Objects.equals(o1, o2)) {
      return 0;
    }
    if (o1 == null) {
      return 1;
    } else if (o2 == null) {
      return -1;
    }

    String name1 = o1.getName();
    String name2 = o2.getName();

    if (name1 == null && name2 == null) {
      return 0;
    } else if (name1 == null) {
      return 1;
    } else if (name2 == null) {
      return -1;
    }

    return name1.compareTo(name2);
  }
}
