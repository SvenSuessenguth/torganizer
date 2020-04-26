package org.cc.torganizer.frontend.disciplines.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;

@RequestScoped
@Named
public class AddOpponentToDiscipline extends DisciplinesAction {

  public void execute(Opponent opponent) {
    Discipline discipline = state.getDiscipline();
    discipline.addOpponent(opponent);
    disciplinesRepository.update(discipline);
  }
}
