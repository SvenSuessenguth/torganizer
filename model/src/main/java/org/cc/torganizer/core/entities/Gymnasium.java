package org.cc.torganizer.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Gymnasium.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Gymnasium")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "GYMNASIUMS")
@NamedQueries({
    @NamedQuery(name = "getAllForTournament", query = "SELECT g FROM Gymnasium g WHERE g.tournament.id = (:tournamentId)") })
public class Gymnasium implements Serializable {
  /** serialVersionUID. */
  private static final long serialVersionUID = -1258367644977462487L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "GYMNASIUM_ID")
  private Long id;

  @Column(name = "GYMNASIUM_NAME")
  private String name;

  @ManyToOne
  @JoinColumn(name = "TOURNAMENT_ID", nullable = false)
  private Tournament tournament;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "GYMNASIUMS_COURTS", joinColumns = {
      @JoinColumn(name = "GYMNASIUM_ID") }, inverseJoinColumns = @JoinColumn(name = "COURT_ID"))
  private List<Court> courts = new ArrayList<Court>();

  /**
   * Default.
   */
  public Gymnasium() {
    // gem. Bean-Spec.
  }

  /**
   * Erzeugen einer vorgegebenen Anzahl von Courts. Die bereits bestehenden
   * Courts werden verworfen.
   * 
   * @param number Anzahl der Courts, die erzeugt werden sollen.
   */
  public void createCourts(int number) {
    if (number != courts.size()) {
      courts.clear();
      for (int i = 0; i < number; i += 1) {
        Court court = new Court();
        court.setGymnasium(this);
        court.setNr(i + 1);
        courts.add(court);
      }
    }
  }

  public int getCourtsCount() {
    return courts.size();
  }

  public List<Court> getCourts() {
    return courts;
  }

  public Tournament getTournament() {
    return tournament;
  }

  public void setTournament(Tournament pTournament) {
    this.tournament = pTournament;
  }

  public String getName() {
    return name;
  }

  public void setName(String pName) {
    this.name = pName;
  }

  public Long getId() {
    return id;
  }

  public void setId(long newId) {
    this.id = newId;
  }
}
