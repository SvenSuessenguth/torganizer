package org.cc.torganizer.frontend.clubs;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
public class ClubsStateProducer {

  @Inject
  private ClubsStateSynchronizer synchronizer;

  @Produces
  @ViewScoped
  @Default
  @Named
  public ClubsState clubsState() {
    ClubsState state = new ClubsState();
    synchronizer.synchronize(state);

    return state;
  }
}
