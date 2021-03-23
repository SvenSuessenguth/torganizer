package org.cc.torganizer.frontend.clubs;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

@RequestScoped
public class ClubsStateProducer {

  @Inject
  private ClubsStateSynchronizer synchronizer;

  @Produces
  @ViewScoped
  @Default
  public ClubsState produce() {
    ClubsState state = new ClubsState();
    synchronizer.synchronize(state);

    return state;
  }
}
