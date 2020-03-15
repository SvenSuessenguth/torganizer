package org.cc.torganizer.frontend.tournaments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Tournament;
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

  private List<Tournament> tournaments = new ArrayList<>();

  private Tournament current;

  /**
   * new name to set for current or new tournament.
   */
  private String currentsNewName;

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
      logger.info("tournaments state is inited with the current tounament '{}'", current.getName());
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

  public void setCurrentsNewName(String currentsNewName) {
    this.currentsNewName = currentsNewName;
  }

  public String getCurrentsNewName() {
    return currentsNewName;
  }
}
