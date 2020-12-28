package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;

/**
 * Saving the current round.
 */
@RequestScoped
@Named
public class SaveRoundAction extends RoundsAction {

  /**
   * Functional Interface method.
   */
  public void execute() {
    Round round = roundsState.getRound();

    if (round.getId() == null) {
      Discipline discipline = disciplinesState.getDiscipline();
      discipline.addRound(round);
      disciplinesRepository.update(discipline);
    } else {
      roundsRepository.update(round);
    }
  }
}
