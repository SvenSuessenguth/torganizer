package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.frontend.disciplines.rounds.RoundsState;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.persistence.RoundsRepository;

/**
 * Providing States and Repositories for editing Rounds.
 */
public abstract class RoundsAction {

  @Inject
  protected RoundsState roundsState;

  @Inject
  protected DisciplinesCoreState disciplinesCoreState;

  @Inject
  protected RoundsRepository roundsRepository;

  @Inject
  protected DisciplinesRepository disciplinesRepository;

  @Inject
  protected ApplicationState applicationState;
}
