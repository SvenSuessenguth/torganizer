package org.cc.torganizer.frontend.players;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Producing a serializable and initialized state.
 */
@RequestScoped
public class PlayersStateProducer {

  @Inject
  private PlayersStateSynchronizer synchronizer;

  /**
   * Producing a serializable and initialized state.
   */
  @Produces
  @ViewScoped
  @Default
  @Named
  public PlayersState playersState() {
    PlayersState state = new PlayersState();
    synchronizer.synchronize(state);

    return state;
  }
}
