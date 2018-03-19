package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.Opponent;
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

    return o1.getPerson().getLastName().compareTo(o2.getPerson().getLastName());
  }
}
