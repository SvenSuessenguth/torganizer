package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesState;
import org.cc.torganizer.persistence.DisciplinesRepository;

/**
 * Providing states and repositories for DisciplineActions.
 */
public abstract class DisciplinesAction {

  @Inject
  protected DisciplinesState state;

  @Inject
  protected DisciplinesRepository disciplinesRepository;

  @Inject
  protected ApplicationState applicationState;
}
