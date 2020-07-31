package org.cc.torganizer.core;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.System;

import java.util.List;
import java.util.Set;

public interface OpponentToGroupsAssigner {
  void assign(Set<Opponent> opponents, List<Group> groups);

  System getSystem();
}