package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;

/**
 * Cancel editing the current discipline.
 */
@RequestScoped
@Named
public class CancelDiscipline {

  @Inject
  private DisciplinesCoreState state;

  /**
   * cancel.
   */
  public String execute() {
    var discipline = new Discipline();
    discipline.addRestriction(new GenderRestriction());
    discipline.addRestriction(new OpponentTypeRestriction());
    discipline.addRestriction(new AgeRestriction());

    state.setDiscipline(discipline);

    return null;
  }
}
