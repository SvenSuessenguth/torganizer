package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * Synchronizing the application state.
 */
@RequestScoped
@Named
public class SynchronizePlayerState extends PlayersAction {

  /**
   * synchronizing the state with database.
   */
  public String execute() {
    if (applicationState.getTournament() == null || applicationState.getTournamentId() == null) {
      return "tournament";
    }

    state.synchronize();
    return null;
  }
}
