package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * Synchronizing the Squads state with the Database.
 */
@RequestScoped
@Named
public class SynchronizeSquadsStateAction extends SquadsAction {

  /**
   * synchronizing the state with the database.
   */
  public String execute() {
    if (applicationState.getTournament() == null || applicationState.getTournamentId() == null) {
      return "tournament";
    }

    state.synchronize();
    return null;
  }
}
