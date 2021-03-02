package org.cc.torganizer.frontend.players.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.players.PlayersState;
import org.cc.torganizer.frontend.players.PlayersStateSynchronizer;

/**
 * Synchronizing the application state.
 */
@RequestScoped
@Named
public class SynchronizePlayerState {

  @Inject
  private PlayersStateSynchronizer synchronizer;

  @Inject
  protected PlayersState state;

  @Inject
  protected ApplicationState applicationState;

  /**
   * synchronizing the state with database.
   */
  public String execute() {
    if (applicationState.getTournament() == null || applicationState.getTournamentId() == null) {
      return "tournament";
    }

    synchronizer.synchronize(state);
    return null;
  }
}
