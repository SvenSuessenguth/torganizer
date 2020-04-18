package org.cc.torganizer.frontend.clubs.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

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
