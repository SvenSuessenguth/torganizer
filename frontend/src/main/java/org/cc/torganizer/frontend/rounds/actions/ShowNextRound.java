package org.cc.torganizer.frontend.rounds.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;

@RequestScoped
@Named
public class ShowNextRound extends RoundsAction {

  public void execute() {
    Round round = roundsState.getRound();
    Discipline discipline = disciplinesState.getDiscipline();

    int position = round.getPosition();

    Round newRound = discipline.getRound(position + 1);
    roundsState.setRound(newRound);
  }
}
