package org.cc.torganizer.frontend.players;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Accessing data from outside the state or the state itself.
 */
@RequestScoped
@Named
public class PlayersBacking {

  @Inject
  private PlayersState state;

  public PlayersState getState() {
    return state;
  }
}
