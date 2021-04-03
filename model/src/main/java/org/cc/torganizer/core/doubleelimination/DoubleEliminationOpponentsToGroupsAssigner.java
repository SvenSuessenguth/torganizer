package org.cc.torganizer.core.doubleelimination;

import static org.cc.torganizer.core.entities.System.DOUBLE_ELIMINATION;

import java.util.List;
import java.util.Set;
import org.cc.torganizer.core.OpponentToGroupsAssigner;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.System;

public class DoubleEliminationOpponentsToGroupsAssigner implements OpponentToGroupsAssigner {

  @Override
  public void assign(Set<Opponent> opponents, List<Group> groups) {
  }

  @Override
  public System getSystem() {
    return DOUBLE_ELIMINATION;
  }
}
