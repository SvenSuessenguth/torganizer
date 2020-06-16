package org.cc.torganizer.frontend.rounds.actions;

import javax.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.disciplines.DisciplinesState;
import org.cc.torganizer.frontend.rounds.RoundsState;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.persistence.RoundsRepository;

public abstract class RoundsAction {

  @Inject
  protected RoundsState roundsState;

  @Inject
  protected DisciplinesState disciplinesState;

  @Inject
  protected RoundsRepository roundsRepository;

  @Inject
  protected DisciplinesRepository disciplinesRepository;

  @Inject
  protected ApplicationState applicationState;
}
