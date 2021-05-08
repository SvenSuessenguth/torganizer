package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.persistence.DisciplinesRepository;

/**
 * Removing Opponent from Discipline.
 */
@RequestScoped
@Named
public class RemoveOpponentFromDiscipline {

  @Inject
  private DisciplinesCoreState state;

  @Inject
  private DisciplinesRepository disciplinesRepository;

  /**
   * Removing an opponent from the current discipline.
   */
  public void execute(Opponent opponent) {
    var discipline = state.getDiscipline();
    discipline.removeOpponent(opponent);
    disciplinesRepository.update(discipline);
  }
}
