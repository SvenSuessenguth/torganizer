package org.cc.torganizer.frontend.players;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

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
  public PlayersState produces() {
    PlayersState state = new PlayersState();
    synchronizer.synchronize(state);

    return state;
  }
}
