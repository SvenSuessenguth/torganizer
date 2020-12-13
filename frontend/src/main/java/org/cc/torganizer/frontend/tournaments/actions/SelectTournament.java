package org.cc.torganizer.frontend.tournaments.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Tournament;

/**
 * Selecting a Tournament from UI.
 */
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
