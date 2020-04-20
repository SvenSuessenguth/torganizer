package org.cc.torganizer.frontend;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Tournament;

@ConversationScoped
@Named
public class ApplicationState implements Serializable {

  private Tournament tournament;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  public void synchronize() {
  }

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
