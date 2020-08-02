package org.cc.torganizer.frontend.clubs.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;

/**
 * Action to create a club.
 */
@RequestScoped
@Named
public class CancelClub extends ClubsAction {

  /**
   * creating a club.
   */
  public String execute() {
    Club club = new Club();
    state.setCurrent(club);

    return null;
  }
}
