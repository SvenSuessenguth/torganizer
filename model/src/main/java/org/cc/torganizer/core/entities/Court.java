package org.cc.torganizer.core.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Spielfeld.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Court")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "COURTS")
@NamedQueries({
    @NamedQuery(name = "getCourtsForGymnasium", query = "SELECT c FROM Court c WHERE c.gymnasium = (:gymnasium)"),
    @NamedQuery(name = "getCourtsWithMatchForTournament", query = "SELECT c FROM Court c WHERE c.match IS NOT NULL AND c.gymnasium.tournament.id = (:tournamentId)"),
    @NamedQuery(name = "getCourtForMatch", query = "SELECT c FROM Court c WHERE c.match = (:match)"),
    @NamedQuery(name = "getUnusedCourtsForGymnasium", query = "SELECT c FROM Court c WHERE c.match IS null AND c.gymnasium = (:gymnasium)") })
public class Court
  extends AbstractBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "COURT_ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "GYMNASIUM_ID", nullable = false)
  private Gymnasium gymnasium;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "MATCH_ID", nullable = true)
  private Match match;

  @Column(name = "NR", nullable = false)
  private int nr;

  /**
   * Default.
   */
  public Court() {
    // gem. Bean-Spec.
  }

  public Match getMatch() {
    return match;
  }

  public void setMatch(Match newMatch) {
    this.match = newMatch;
  }

  public int getNr() {
    return nr;
  }

  public void setNr(int newNr) {
    this.nr = newNr;
  }

  @Override
  public Long getId() {
    return this.id;
  }
  
  @Override
  public void setId(Long newId) {
    this.id = newId;
  }

  public Gymnasium getGymnasium() {
    return gymnasium;
  }

  public void setGymnasium(Gymnasium pGymnasium) {
    this.gymnasium = pGymnasium;
  }
}
