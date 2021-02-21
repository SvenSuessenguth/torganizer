package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.squads.SquadsState;
import org.cc.torganizer.frontend.squads.SquadsStateSynchronizer;

/**
 * Synchronizing the Squads state with the Database.
 */
@RequestScoped
@Named
@SuppressWarnings("unused")
public class SynchronizeSquadsStateAction {

  @Inject
  protected SquadsState state;

  @Inject
  protected ApplicationState applicationState;

  @Inject
  private SquadsStateSynchronizer synchronizer;

  /**
   * synchronizing the state with the database.
   */
  public String execute() {
    if (applicationState.getTournament() == null || applicationState.getTournamentId() == null) {
      return "tournament";
    }

    synchronizer.synchronize(state);
    return null;
  }
}
