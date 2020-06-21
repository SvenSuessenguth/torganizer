package org.cc.torganizer.frontend.rounds.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;

@RequestScoped
@Named
public class DeleteRoundAction extends RoundsAction {
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
