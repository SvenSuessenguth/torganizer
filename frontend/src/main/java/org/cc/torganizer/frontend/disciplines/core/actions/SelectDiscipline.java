package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;

/**
 * Selecting a Discipline from the UI.
 */
@RequestScoped
@Named
public class SelectDiscipline {

  @Inject
  private DisciplinesCoreState state;

  public void execute(Discipline discipline) {
    state.setDiscipline(discipline);
  }
}
