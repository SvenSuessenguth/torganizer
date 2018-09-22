package org.cc.torganizer.core.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Wegen Restriktionen der Persistenz-API k\u00f6nnen keine Interfaces verwendet
 * werden. Daher muss im Objektmodell der Player ein Opponent mit Person als
 * Attribut sein.
 */
public class Player extends Opponent {

  private Person person;

  private Club club;

  private LocalDateTime lastMatch;

  /**
   * default.
   */
  public Player() {
    // gem. Bean-Spec.
  }

  public Player(Long id) {
    setId(id);
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
   * @param lastName  Nachname
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Player> getPlayers() {
    Set<Player> players = new HashSet<>();
    players.add(this);

    return players;
  }

  public LocalDateTime getLastMatch() {
    return lastMatch;
  }

  public void setLastMatch(LocalDateTime lastMatch) {
    this.lastMatch = lastMatch;
  }

  /**
   * Prueft, ob das Gender des Players bekannt ist (also nicht UNKNOWN).
   *
   * @return <code>true</code>, wenn das Gender der Person nicht
   *     <code>null</code> und nicht unbekannt ist, sonst <code>false</code>
   */
  public boolean hasGender() {

    boolean isGenderSet = true;

    Gender gender = getPerson().getGender();
    if (gender == null || Gender.UNKNOWN.equals(gender)) {
      isGenderSet = false;
    }

    return isGenderSet;
  }

  public Club getClub() {
    return club;
  }

  public void setClub(Club club) {
    this.club = club;
  }

  public OpponentType getOpponentType() {
    return OpponentType.PLAYER;
  }

  @Override
  public String toString() {
    return "[" + getId() + "] " + person.getLastName() + ", " + person.getFirstName();
  }
}
