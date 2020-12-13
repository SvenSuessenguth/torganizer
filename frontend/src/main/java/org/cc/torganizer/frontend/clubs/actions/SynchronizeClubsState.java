package org.cc.torganizer.frontend.clubs.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * Initializing the clubs staten.
 */
@RequestScoped
@Named
public class SynchronizeClubsState extends ClubsAction {

  public void execute() {
    state.synchronize();
  }
}
