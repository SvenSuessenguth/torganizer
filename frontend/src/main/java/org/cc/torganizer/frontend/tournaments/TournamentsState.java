package org.cc.torganizer.frontend.tournaments;

import jakarta.enterprise.inject.Vetoed;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.entities.Tournament;

/**
 * The current tournament is used for every following action. So this bean is in session-scope.
 */
@Vetoed
public class TournamentsState implements Serializable {

  private List<Tournament> tournaments = new ArrayList<>();
  private Tournament current;

  public void setTournaments(List<Tournament> tournaments) {
    this.tournaments = tournaments;
  }

  public List<Tournament> getTournaments() {

    System.out.println("hallo du da");
    return tournaments;
  }

  public Tournament getCurrent() {
    return current;
  }

  public void setCurrent(Tournament current) {
    this.current = current;
  }
}
