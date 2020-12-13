package org.cc.torganizer.frontend.clubs.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Club;

/**
 * Set the selected Club to the State.
 */
@RequestScoped
@Named
public class SelectClub extends ClubsAction {

  public void execute(Club club) {
    state.setCurrent(club);
  }
}
