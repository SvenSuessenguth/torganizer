package org.cc.torganizer.frontend.squads.actions;

import jakarta.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.squads.SquadsState;

public class SquadAction {

  @Inject
  ApplicationState applicationState;

  @Inject
  SquadsState state;
}
