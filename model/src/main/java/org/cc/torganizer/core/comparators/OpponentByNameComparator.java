package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;

import java.io.Serializable;
import java.util.*;

public class OpponentByNameComparator implements Comparator<Opponent>, Serializable {

  private PlayerByNameComparator pbnComparator = new PlayerByNameComparator();

  @Override
  public int compare(Opponent o1, Opponent o2) {

    if(Objects.equals(o1, o2)){
      return 0;
    }

    List<Player> o1Players = new ArrayList<>(o1.getPlayers());
    List<Player> o2Players = new ArrayList<>(o2.getPlayers());

    if(o1Players.isEmpty() && o2Players.isEmpty()){
      return 0;
    }else if(o1Players.isEmpty()){
      return -1;
    }else if(o2Players.isEmpty()){
      return 1;
    }

    Collections.sort(o1Players, pbnComparator);
    Collections.sort(o2Players, pbnComparator);

    Player po1 = o1Players.get(0);
    Player p02 = o2Players.get(0);

    return pbnComparator.compare(po1, p02);
  }
}
