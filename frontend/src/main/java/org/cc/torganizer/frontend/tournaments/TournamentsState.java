package org.cc.torganizer.frontend.tournaments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
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
@ConversationScoped
public class TournamentsState implements Serializable {

  private static final long serialVersionUID = -221858086249641203L;

  @Inject
  @Online
  private SimplifiedLogger log;

  @Inject
  private transient TournamentsRepository tournamentsRepository;

  @Inject
  private Conversation conversation;

  private List<Tournament> tournaments = new ArrayList<>();
  private Tournament current;

  /**
   * Load tournaments to show on view.
   */
  @PostConstruct
  public void postConstruct() {
    log.severe("Conversation ist noch nicht gestartet");

    conversation.begin();

    log.severe("Conversation wurde gestartet");

    tournaments = tournamentsRepository.read(0, 100);

    if (current == null && !this.tournaments.isEmpty()) {
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
