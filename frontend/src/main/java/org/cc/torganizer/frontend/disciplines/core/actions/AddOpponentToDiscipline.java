package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.persistence.DisciplinesRepository;

/**
 * Adding an opponent to a Discipline.
 */
@RequestScoped
@Named
public class AddOpponentToDiscipline {

  @Inject
  private DisciplinesRepository disciplinesRepository;

  @Inject
  private DisciplinesCoreState state;

  /**
   * Adding an opponent to the current discipline.
   */
  public void execute(Opponent opponent) {
    var discipline = state.getDiscipline();
    discipline.addOpponent(opponent);
    disciplinesRepository.update(discipline);
  }
}
