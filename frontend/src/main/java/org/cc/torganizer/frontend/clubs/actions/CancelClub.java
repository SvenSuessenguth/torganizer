package org.cc.torganizer.frontend.clubs.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Club;

/**
 * Action to create a club.
 */
@RequestScoped
@Named
public class CancelClub extends ClubsAction {

  /**
   * creating a new club and replace the club currently under work.
   */
  public String execute() {
    var club = new Club();
    state.setCurrent(club);

    return null;
  }
}
