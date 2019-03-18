package org.cc.torganizer.core.roundrobin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.cc.torganizer.core.OpponentToGroupsAssigner;
import org.cc.torganizer.core.comparators.OpponentsByClubComparator;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.System;

public class RoundRobinOpponentsToGroupsAssigner implements OpponentToGroupsAssigner {

  @Override
  public void assign(Set<Opponent> opponents, List<Group> groups) {
    // early exit
    if (opponents == null || opponents.isEmpty() || groups == null || groups.isEmpty()) {
      return;
    }


    // avoid/minimize matches with opponents of the same club
    List<Opponent> sortedOpponents = new ArrayList<>(opponents);
    Collections.sort(sortedOpponents, new OpponentsByClubComparator());

    int groupsCount = groups.size();
    int groupIndex = 0;
    for (Opponent opponent : sortedOpponents) {
      Group group = groups.get(groupIndex % groupsCount);

      group.addOpponent(opponent);

      groupIndex += 1;
    }
  }

  @Override
  public System getSystem() {
    return System.ROUND_ROBIN;
  }
}
