package org.cc.torganizer.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

/**
 * Wegen Restriktionen der Persistenz-API können keine Interfaces verwendet
 * werden. Daher muss im Objektmodell der Player ein Opponent mit Person als
 * Attribut sein.
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Player extends Opponent {
  @Builder.Default
  private Person person = Person.builder().build();
  private Club club;
  private LocalDateTime lastMatchTime;

  public Player() {
    person = null;
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

  /**
   * convenience constructor.
   *
   * @param firstName Vorname
   * @param lastName  Nachname
   * @param club      Club
   */
  public Player(String firstName, String lastName, Club club) {
    this.person = new Person(firstName, lastName);
    this.club = club;
  }

  @Override
  public Set<Player> getPlayers() {
    var players = new HashSet<Player>();
    players.add(this);

    return players;
  }

  /**
   * Prueft, ob das Gender des Players bekannt ist (also nicht UNKNOWN).
   *
   * @return <code>true</code>, wenn das Gender der Person nicht
   * <code>null</code> und nicht unbekannt ist, sonst <code>false</code>
   */
  public boolean hasGender() {

    var isGenderSet = true;

    var gender = getPerson().getGender();
    if (gender == null || UNKNOWN == gender) {
      isGenderSet = false;
    }

    return isGenderSet;
  }

  public OpponentType getOpponentType() {
    return OpponentType.PLAYER;
  }

  @Override
  public String toString() {
    var builder = new StringBuilder("[" + getId() + "] ");
    if (this.person != null) {
      builder.append(person.getLastName());
      builder.append(", ");
      builder.append(person.getFirstName());
    }

    return builder.toString();
  }
}
