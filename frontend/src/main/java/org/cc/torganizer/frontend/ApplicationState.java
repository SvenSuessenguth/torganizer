package org.cc.torganizer.frontend;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.persistence.ClubsRepository;

@ConversationScoped
@Named
public class ApplicationState implements Serializable {

  @Inject
  private ClubsRepository clubsRepository;

  private Tournament tournament;

  private long clubsCount = 0L;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  public void synchronize() {
    clubsCount = clubsRepository.count();
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

  public long getClubsCount() {
    return clubsCount;
  }
}
