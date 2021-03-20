package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundStateSynchronizer;

/**
 * Selecting a Discipline from the UI.
 */
@RequestScoped
@Named
public class SelectDiscipline {

  @Inject
  private DisciplinesCoreState state;

  @Inject
  private DisciplineRoundState roundState;

  @Inject
  private DisciplineRoundStateSynchronizer roundStateSynchronizer;

  public void execute(Discipline discipline) {
    state.setDiscipline(discipline);

    roundStateSynchronizer.synchronize(roundState);
  }
}
