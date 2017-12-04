package org.cc.torganizer.core.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;

/**
 * Wegen Restriktionen der Persistenz-API k\u00f6nnen keine Interfaces verwendet
 * werden. Daher muss im Objektmodell der Player ein Opponent mit Person als
 * Attribut sein.
 */
public class Player extends Opponent {

  @JsonbProperty
  private Person person;

  @JsonbProperty(nillable = true)
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
  @JsonbTransient
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
