package org.cc.torganizer.frontend.tournaments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * The current tournament is used for every following action. So this bean is in session-scope.
 */
@Named
@ViewScoped
public class TournamentsState implements Serializable {

  private static final long serialVersionUID = 4070827997380138970L;

  @Inject
  private transient Logger logger;

  @Inject
  private transient TournamentsRepository tournamentsRepository;

  @Inject
  private ApplicationState appState;

  private List<Tournament> tournaments = new ArrayList<>();

  private Tournament current;

  /**
   * new name to set for current or new tournament.
   */
  private String currentName;

  public void initState() {
    tournaments = tournamentsRepository.read(0, 100);

    if (current != null && current.getId() != null) {
      return;
    }

    if ((current == null || current.getId() == null) && !this.tournaments.isEmpty()) {
      current = this.tournaments.get(0);
      currentName = current.getName();
      appState.setCurrent(current);
      logger.info("tournaments state is inited with the current tounament '{}'", current.getName());
    } else {
      current = new Tournament();
      current.setName("Tournament");
      currentName = current.getName();
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

  public void setCurrentName(String currentName) {
    this.currentName = currentName;
  }

  public String getCurrentName() {
    return currentName;
  }
}
