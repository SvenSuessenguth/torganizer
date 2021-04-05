package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.inject.Inject;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;

/**
 * Support for access on round specific states.
 */
public abstract class RoundAction {

  @Inject
  protected DisciplineRoundState roundState;

  @Inject
  protected DisciplinesCoreState coreState;
}
