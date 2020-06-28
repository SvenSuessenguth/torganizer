package org.cc.torganizer.frontend.rounds.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Round;

@RequestScoped
@Named
public class CancelRoundAction extends RoundsAction {
  public void execute() {
    Round round = new Round();

    roundsState.setRound(round);
  }
}
