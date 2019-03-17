package org.cc.torganizer.core.roundrobin;

import java.util.List;
import java.util.Set;

import org.cc.torganizer.core.OpponentToGroupsAssigner;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.System;

public class RoundRobinOpponentsToGroupsAssigner implements OpponentToGroupsAssigner {

  @Override
  public void assign(Set<Opponent> opponents, List<Group> groups) {
    // avoid/minimize matches with opponents of the same club


  }

  @Override
  public System getSystem() {
    return System.ROUND_ROBIN;
  }
}
