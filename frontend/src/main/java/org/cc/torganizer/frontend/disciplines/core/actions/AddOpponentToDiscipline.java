package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;

/**
 * Adding an opponent to a Discipline.
 */
@RequestScoped
@Named
public class AddOpponentToDiscipline extends DisciplinesAction {

  /**
   * Adding an opponent to the current discipline.
   */
  public void execute(Opponent opponent) {
    Discipline discipline = state.getDiscipline();
    discipline.addOpponent(opponent);
    disciplinesRepository.update(discipline);
  }
}
