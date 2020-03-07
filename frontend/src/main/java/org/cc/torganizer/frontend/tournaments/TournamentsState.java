package org.cc.torganizer.frontend.tournaments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.logging.SimplifiedLogger;
import org.cc.torganizer.frontend.logging.online.Online;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * The current tournament is used for every following action. So this bean is in session-scope.
 */
@Named
@ViewScoped
public class TournamentsState implements Serializable {

  private static final long serialVersionUID = 4070827997380138970L;

  @Inject
  @Online
  private SimplifiedLogger log;

  @Inject
  private transient TournamentsRepository tournamentsRepository;

  private List<Tournament> tournaments = new ArrayList<>();
  private Tournament current;

  /**
   * Load tournaments to show on view.
   */
  @PostConstruct
  public void postConstruct() {
    initState();
  }

  protected void initState() {
    tournaments = tournamentsRepository.read(0, 100);

    if (current == null && !this.tournaments.isEmpty()) {
      current = this.tournaments.get(0);
    }
    if (tournaments.isEmpty()) {
      throw new RuntimeException("no tournament");
    }

    log.info("tournaments state is inited with the current tounament '" + current.getName() + "'");
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
