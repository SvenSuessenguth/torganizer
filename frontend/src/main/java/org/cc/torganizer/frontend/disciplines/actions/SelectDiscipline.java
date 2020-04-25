package org.cc.torganizer.frontend.disciplines.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;

@RequestScoped
@Named
public class SelectDiscipline extends DisciplinesAction {

  public void execute(Discipline discipline) {
    state.setDiscipline(discipline);
  }
}
