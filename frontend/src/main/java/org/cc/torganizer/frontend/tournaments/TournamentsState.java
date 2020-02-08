package org.cc.torganizer.frontend.tournaments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * The current tournament is used for every following action. So this bean is in session-scope.
 */
@Named
@SessionScoped
public class TournamentsState implements Serializable {

  @Inject
  TournamentsRepository tournamentsRepository;

  private List<Tournament> tournaments = new ArrayList<>();
  private Tournament current;

  @PostConstruct
  public void postConstruct() {

    tournaments = tournamentsRepository.read(0, 100);

    if (current == null && this.tournaments.size() > 0) {
      current = this.tournaments.get(0);
    }
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
