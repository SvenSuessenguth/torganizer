package org.cc.torganizer.core.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wegen Restriktionen der Persistenz-API k\u00f6nnen keine Interfaces verwendet
 * werden. Daher muss im Objektmodell der Player ein Opponent mit Person als
 * Attribut sein.
 */
@XmlRootElement(name = "Player")
@XmlAccessorType(XmlAccessType.FIELD)
public class Player extends Opponent {

  private Person person;

  private LocalDateTime lastMatch;

  /**
   * default.
   */
  public Player() {
    // gem. Bean-Spec.
  }

  /**
   * convenience constructor.
   * 
   * @param newPerson holds the players person-data
   */
  public Player(Person newPerson) {
    this.person = newPerson;
  }

  /**
   * convenience constructor.
   * 
   * @param firstName Vorname
   * @param lastName Nachname
   */
  public Player(String firstName, String lastName) {
    this.person = new Person(firstName, lastName);
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person newPerson) {
    this.person = newPerson;
  }

  /** {@inheritDoc} */
  @Override
  public List<Player> getPlayers() {
    List<Player> players = new ArrayList<>();
    players.add(this);

    return players;
  }

  public LocalDateTime getLastMatch() {
    return lastMatch;
  }

  public void setLastMatch(LocalDateTime pLastMatch) {
    this.lastMatch = pLastMatch;
  }
}
