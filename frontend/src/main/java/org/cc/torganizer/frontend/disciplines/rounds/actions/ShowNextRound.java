package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * Showing the next round.
 */
@RequestScoped
@Named
public class ShowNextRound extends RoundAction {

  /**
   * Functional Interface method.
   */
  public void execute() {
    var round = roundState.getRound();
    var discipline = coreState.getDiscipline();

    int position = round.getPosition();

    var newRound = discipline.getRound(position + 1);
    roundState.setRound(newRound);
  }
}
