package org.cc.torganizer.frontend.rounds.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;

@RequestScoped
@Named
public class SaveRoundAction extends RoundsAction {

  public void execute() {
    Discipline discipline = disciplinesState.getDiscipline();
    Round round = new Round();
    roundsRepository.create(round);

    discipline.addRound(round);
    disciplinesRepository.update(discipline);

    roundsState.setRound(round);
  }
}
