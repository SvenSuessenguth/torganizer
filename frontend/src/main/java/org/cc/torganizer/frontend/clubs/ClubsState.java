package org.cc.torganizer.frontend.clubs;

import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;

@ViewScoped
@Named
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
