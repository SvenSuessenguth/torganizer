package org.cc.torganizer.frontend.clubs;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.persistence.ClubsRepository;

@ViewScoped
@Named
public class ClubsState extends State implements Serializable {

  private List<Club> clubs;
  private Club current;

  @Inject
  private ClubsRepository clubsRepository;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  @Override
  public void synchronize() {
    clubs = clubsRepository.read(0, 1000);
    current = new Club();
  }

  public List<Club> getClubs() {
    return clubs;
  }

  public void setClubs(List<Club> clubs) {
    this.clubs = clubs;
  }

  public Club getCurrent() {
    return current;
  }

  public void setCurrent(Club current) {
    this.current = current;
  }
}
