package org.cc.torganizer.frontend.clubs.actions;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;

@RequestScoped
@Named
public class InitClubsState extends ClubsAction {

  public void execute() {
    List<Club> clubs = clubsRepository.read(0, 1000);
    state.setClubs(clubs);

    if (state.getCurrent() == null && !clubs.isEmpty()) {
      state.setCurrent(clubs.get(0));
    }
  }

}
