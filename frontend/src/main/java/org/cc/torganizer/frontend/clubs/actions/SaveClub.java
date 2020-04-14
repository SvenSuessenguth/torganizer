package org.cc.torganizer.frontend.clubs.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;

@RequestScoped
@Named
public class SaveClub extends ClubsAction {

  public void execute() {
    Club current = state.getCurrent();
    clubsRepository.update(current);
  }
}
