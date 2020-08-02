package org.cc.torganizer.frontend.disciplines.rounds.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Round;

/**
 * Canceling editing the current round.
 */
@RequestScoped
@Named
public class CancelRoundAction extends RoundsAction {

  /**
   * Execute.
   */
  public void execute() {
    Round round = new Round();

    roundsState.setRound(round);
  }
}
