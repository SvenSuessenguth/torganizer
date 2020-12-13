package org.cc.torganizer.frontend.clubs;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.persistence.ClubsRepository;

/**
 * State for editing Clubs.
 */
@ViewScoped
@Named
public class ClubsState implements State, Serializable {

  private List<Club> clubs;
  private Club current;

  @Inject
  private ClubsRepository clubsRepository;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  @Override
  public void synchronize() {
    clubs = clubsRepository.read(0, 1000);
    current = new Club();
  }

  public List<Club> getClubs() {
    return clubs;
  }

  public void setClubs(List<Club> clubs) {
    this.clubs = clubs;
  }

  public Club getCurrent() {
    return current;
  }

  public void setCurrent(Club current) {
    this.current = current;
  }
}
