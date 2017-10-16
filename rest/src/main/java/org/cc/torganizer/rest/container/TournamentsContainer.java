package org.cc.torganizer.rest.container;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cc.torganizer.core.entities.Tournament;

/**
 * Container für Restrictions, um korrektes XML/JSON zu generieren.
 *  
 * @author svens
 */
@XmlRootElement(name="tournaments")
public class TournamentsContainer {

  @XmlElement
  private List<Tournament> tournaments = new ArrayList<>();

  public TournamentsContainer() {
  }

  public TournamentsContainer(List<Tournament> tournaments) {
    this.tournaments.addAll(tournaments);
  }

  public List<Tournament> getTournaments() {
    return tournaments;
  }

  public void setTournaments(List<Tournament> tournaments) {
    this.tournaments = tournaments;
  }

  public void addAll(List<Tournament> tournaments) {
    this.tournaments.addAll(tournaments);
  }

}
