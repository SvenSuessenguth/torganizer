package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;

/**
 * Selecting a Discipline from the UI.
 */
@RequestScoped
@Named
public class SelectDiscipline extends DisciplinesAction {

  public void execute(Discipline discipline) {
    state.setDiscipline(discipline);
  }
}
