package org.cc.torganizer.frontend.clubs;

import jakarta.enterprise.inject.Vetoed;
import java.io.Serializable;
import java.util.List;
import org.cc.torganizer.core.entities.Club;

/**
 * State for editing Clubs.
 */
@Vetoed
public class ClubsState implements Serializable {

  private List<Club> clubs;
  private Club current;

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
