package org.cc.torganizer.frontend.clubs;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class ClubsBacking {

  @Inject
  private ClubsState state;

  public ClubsState getState() {
    return state;
  }
}
