package org.cc.torganizer.frontend.tournaments.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.tournaments.TournamentsState;

/**
 * Selecting a Tournament from UI.
 */
@RequestScoped
@Named
public class SelectTournament {

  @Inject
  protected TournamentsState state;
  @Inject
  protected ApplicationState appState;

  /**
   * select tournament in UI.
   */
  public void execute(Tournament selected) {
    state.setCurrent(selected);

    appState.setTournament(selected);
  }
}
