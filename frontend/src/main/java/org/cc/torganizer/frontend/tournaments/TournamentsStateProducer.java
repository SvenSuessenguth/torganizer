package org.cc.torganizer.frontend.tournaments;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
public class TournamentsStateProducer {

  @Inject
  private TournamentsStateSynchronizer synchronizer;

  @ViewScoped
  @Default
  @Named
  @Produces
  public TournamentsState produce() {
    TournamentsState state = new TournamentsState();
    synchronizer.synchronize(state);

    return state;
  }
}
