package org.cc.torganizer.frontend.disciplines.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;

/**
 * Action to create a club.
 */
@RequestScoped
@Named
public class CancelDiscipline extends DisciplinesAction {

  /**
   * creating a club.
   */
  public String execute() {
    Discipline discipline = new Discipline();
    state.setDiscipline(discipline);

    return null;
  }
}
