package org.cc.torganizer.frontend.tournaments.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Tournament;

@RequestScoped
@Named
public class SelectTournament extends TournamentsAction {

  /**
   * select tournament in UI.
   */
  public void execute(Tournament selected) {
    state.setCurrent(selected);

    appState.setTournament(selected);
  }
}
