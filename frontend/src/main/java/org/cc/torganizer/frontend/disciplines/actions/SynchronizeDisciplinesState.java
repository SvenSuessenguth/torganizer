package org.cc.torganizer.frontend.disciplines.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

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
