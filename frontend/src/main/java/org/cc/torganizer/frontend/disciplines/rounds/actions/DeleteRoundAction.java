package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;

/**
 * Deleting the current round.
 */
@RequestScoped
@Named
public class DeleteRoundAction extends RoundsAction {

  /**
   * Execute.
   */
  public void execute() {
    // only last round in discipline with no opponents can be deleted
    // ... checks are coming
    Round round = roundsState.getRound();

    Discipline discipline = disciplinesState.getDiscipline();
    discipline.removeRound(round);
    disciplinesRepository.update(discipline);

    roundsRepository.delete(round.getId());

    roundsState.synchronize();
  }
}
