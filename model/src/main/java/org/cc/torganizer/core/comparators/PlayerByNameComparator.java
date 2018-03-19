package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.Player;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class PlayerByNameComparator implements Comparator<Player>, Serializable {

  @Override
  public int compare(Player o1, Player o2) {
    if(Objects.equals(o1, o2)){
      return 0;
    }

    if(o1==null){
      return -1;
    }else if(o2==null){
      return 1;
    }

    String o1LastName = o1.getPerson().getLastName().toLowerCase();
    String o2LastName = o2.getPerson().getLastName().toLowerCase();

    return o1LastName.compareTo(o2LastName);
  }
}
