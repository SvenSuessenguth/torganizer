package org.cc.torganizer.frontend.tournaments.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Tournament;

@RequestScoped
@Named
public class CancelTournament extends TournamentsAction {

  /**
   * creating a new tournament.
   */
  public String execute() {
    Tournament newTournament = new Tournament();
    state.setCurrent(newTournament);

    return null;
  }
}
