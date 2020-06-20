package org.cc.torganizer.frontend.disciplines.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;

@RequestScoped
@Named
public class SelectDiscipline extends DisciplinesAction {

  public void execute(Discipline discipline) {
    state.setDiscipline(discipline);

    // some already persisted disciplines have no round...
    if (discipline.getRounds().isEmpty()) {
      discipline.addRound(new Round());
    }
  }
}
