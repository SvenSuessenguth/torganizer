package org.cc.torganizer.frontend.tournaments;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ConversationController;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * The current tournament is used for every following action. So this bean is in session-scope.
 */
@Named
@ViewScoped
public class TournamentsState implements Serializable, State {

  @Inject
  private transient TournamentsRepository tournamentsRepository;

  @Inject
  private transient ConversationController conversationController;

  private List<Tournament> tournaments = new ArrayList<>();
  private Tournament current;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  @Override
  public void synchronize() {
    tournaments = tournamentsRepository.read(0, 100);
    current = new Tournament();

    conversationController.beginConversation();
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
