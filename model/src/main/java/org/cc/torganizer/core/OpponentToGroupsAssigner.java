package org.cc.torganizer.core;

import java.util.List;
import java.util.Set;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.System;

public interface OpponentToGroupsAssigner {
  void assign(Set<Opponent> opponents, List<Group> groups);

  System getSystem();
}