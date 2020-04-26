package org.cc.torganizer.frontend.disciplines.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Cancel editing the current discipline.
 */
@RequestScoped
@Named
public class CancelDiscipline extends DisciplinesAction {

  /**
   * cancel.
   */
  public String execute() {
    state.synchronize();

    return null;
  }
}
