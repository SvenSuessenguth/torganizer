package org.cc.torganizer.frontend.squads;

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
public class SquadsStateProducer {

  @Inject
  private SquadsStateSynchronizer synchronizer;

  /**
   * Producing a serializable and initialized state.
   */
  @Produces
  @ViewScoped
  @Default
  @Named
  public SquadsState squadsState() {
    var state = new SquadsState();
    synchronizer.synchronize(state);

    return state;
  }
}
