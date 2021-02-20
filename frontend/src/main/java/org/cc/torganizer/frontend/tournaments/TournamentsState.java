package org.cc.torganizer.frontend.tournaments;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.entities.Tournament;

/**
 * The current tournament is used for every following action. So this bean is in session-scope.
 */
@Named
@ViewScoped
public class TournamentsState implements Serializable {

  @Inject
  private TournamentsStateSynchronizer synchronizer;

  private List<Tournament> tournaments = new ArrayList<>();
  private Tournament current;

  @PostConstruct
  public void postConstruct() {
    synchronizer.synchronize(this);
  }

  public void setTournaments(List<Tournament> tournaments) {
    this.tournaments = tournaments;
  }

  public List<Tournament> getTournaments() {
    return tournaments;
  }

  public Tournament getCurrent() {
    return current;
  }

  public void setCurrent(Tournament current) {
    this.current = current;
  }
}
