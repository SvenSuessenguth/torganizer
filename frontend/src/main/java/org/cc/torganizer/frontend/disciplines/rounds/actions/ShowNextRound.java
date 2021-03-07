package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;

/**
 * Showing the next round.
 */
@RequestScoped
@Named
public class ShowNextRound extends RoundsAction {

  /**
   * Functional Interface method.
   */
  public void execute() {
    Round round = roundsState.getRound();
    Discipline discipline = disciplinesCoreState.getDiscipline();

    int position = round.getPosition();

    Round newRound = discipline.getRound(position + 1);
    roundsState.setRound(newRound);
  }
}
