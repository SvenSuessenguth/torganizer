package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;

/**
 * Showing the previous round.
 */
@RequestScoped
@Named
public class ShowPreviousRound {

  @Inject
  private DisciplineRoundState roundState;

  @Inject
  private DisciplinesCoreState coreState;

  /**
   * Functional Interface method.
   */
  public void execute() {
    Round round = roundState.getRound();
    Discipline discipline = coreState.getDiscipline();

    int position = round.getPosition();

    Round newRound = discipline.getRound(position - 1);
    roundState.setRound(newRound);
  }
}
