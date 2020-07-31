package org.cc.torganizer.frontend;

import org.cc.torganizer.core.entities.Tournament;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import java.io.Serializable;

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
