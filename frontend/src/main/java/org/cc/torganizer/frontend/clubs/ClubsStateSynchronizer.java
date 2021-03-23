package org.cc.torganizer.frontend.clubs;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.persistence.ClubsRepository;

@RequestScoped
public class ClubsStateSynchronizer {

  @Inject
  private transient ClubsRepository clubsRepository;

  public void synchronize(ClubsState state) {
    List<Club> clubs = clubsRepository.read(0, 1000);
    state.setClubs(clubs);
    state.setCurrent(new Club());
  }
}
