package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * Initializing the clubs staten.
 */
@RequestScoped
@Named
public class SynchronizeDisciplinesState extends DisciplinesAction {

  public void execute() {
    state.synchronize();
  }
}
