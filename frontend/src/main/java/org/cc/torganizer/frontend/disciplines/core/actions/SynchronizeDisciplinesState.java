package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreStateSynchronizer;

/**
 * Initializing the clubs staten.
 */
@RequestScoped
@Named
public class SynchronizeDisciplinesState {

  @Inject
  private DisciplinesCoreStateSynchronizer synchronizer;

  @Inject
  private DisciplinesCoreState state;

  public void execute() {
    synchronizer.synchronize(state);
  }
}
