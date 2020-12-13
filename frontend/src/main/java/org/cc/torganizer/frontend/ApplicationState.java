package org.cc.torganizer.frontend;

import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import org.cc.torganizer.core.entities.Tournament;

/**
 * State of the Tournament-Application.
 */
@ConversationScoped
@Named
public class ApplicationState implements Serializable {

  private Tournament tournament;

  public Tournament getTournament() {
    return tournament;
  }

  public void setTournament(Tournament tournament) {
    this.tournament = tournament;
  }

  public Long getTournamentId() {
    return tournament.getId();
  }
}
