package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;

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
    Discipline discipline = new Discipline();
    discipline.addRestriction(new GenderRestriction());
    discipline.addRestriction(new OpponentTypeRestriction());
    discipline.addRestriction(new AgeRestriction());

    state.setDiscipline(discipline);

    return null;
  }
}
