package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;

import org.cc.torganizer.core.comparators.PositionalComparator;

/**
 * Eine Group sammelt Opponents zusammen. Innerhalb einer Group werden die
 * Matches ausgef\u00fchrt, die den Sieger oder die Opponents, die in die
 * n\u00e4chste Round kommen, bestimmen
 */
public class Group extends Entity implements IPositional {

  private Integer position;

  private List<PositionalOpponent> positionalOpponents = new ArrayList<>();

  /**
   * Ein Match soll nur der Group zugewiesen werden, wenn es persistiert werden
   * soll (running/finished).
   */
  private List<Match> matches = new ArrayList<>();

  /**
   * Default.
   */
  public Group() {
    // gem. Bean-Spec.
  }

  /**
   * Gibt die nach Position sortierte Liste von Opponents zur\u00fcck.
   *
   * @return nach Position sortierte Liste von PositionalOpponents
   */
  public List<PositionalOpponent> getPositionalOpponents() {
    // sortieren nach Position
    positionalOpponents.sort(new PositionalComparator());

    return positionalOpponents;
  }

  /**
   * <p>
   * Setter for the field <code>positionalOpponents</code>.
   * </p>
   *
   * @param newPositionalOpponents a {@link java.util.List} object.
   */
  public void setPositionalOpponents(List<PositionalOpponent> newPositionalOpponents) {
    this.positionalOpponents = newPositionalOpponents;
  }

  /**
   * Liste der Opponents, die zu dieser Group geh\u00f6ren wird neu gesetzt.
   * Alte Inhalte werden dabei gel\u00f6scht.
   *
   * @param newOpponents Opponents, die der Group zugewiesen werden sollen.
   */
  public void setOpponents(List<Opponent> newOpponents) {
    getPositionalOpponents().clear();
    for (Opponent o : newOpponents) {
      PositionalOpponent io = new PositionalOpponent(o, newOpponents.indexOf(o) + 1);
      getPositionalOpponents().add(io);
    }
  }

  /**
   * Lesen der Opponents, die dieser Group zugewiesen sind. Die Liste
   * enth\u00e4lt die Opponents in der durch die Position  vorgegebenen Reihenfolge.
   *
   * @return Liste der Opponents, die dieser Gruppe zugewiesen sind.
   */
  public List<Opponent> getOpponents() {
    List<Opponent> result = new ArrayList<>();
    getPositionalOpponents().sort(new PositionalComparator());

    for (PositionalOpponent io : getPositionalOpponents()) {
      result.add(io.getOpponent());
    }

    return result;
  }

  /**
   * Hinzuf\u00fcgen eines (unindizierten) Opponents. Die neue Position ist so
   * gro\u00df, dass der neue Opponent als letzter angef\u00fcgt wird.
   *
   * @param opponent Opponent, der de Group hinzugef\u00fcgt werden soll.
   */
  public void addOpponent(Opponent opponent) {
    Integer lastPosition = positionalOpponents.size();
    PositionalOpponent positionalOpponent = new PositionalOpponent();
    positionalOpponent.setPosition(lastPosition);
    positionalOpponent.setOpponent(opponent);

    getPositionalOpponents().add(positionalOpponent);
  }

  /**
   * Entfernen eines Opponents aus der Liste aller (indizierten) Opponents.
   *
   * @param opponent Opponent, der entfernt werden soll.
   */
  public void removeOpponent(Opponent opponent) {

    // passenden IndexedOpponent finden und entfernen
    PositionalOpponent poToRemove = getPositionalOpponent(opponent);
    getPositionalOpponents().remove(poToRemove);

    // Neuindizierung
    int newPosition = 0;
    for (PositionalOpponent o : getPositionalOpponents()) {
      o.setPosition(newPosition);
      newPosition += 1;
    }
  }

  /**
   * Lesen des indizierten Opponents, bei dem der Opponent mit dem Parameter
   * \u00fcbereinstimmt.
   *
   * @param opponent Opponent, zu dem der indizierte Opponent gefunden werden
   *                 soll.
   * @return Der IndexedOpponent
   */
  public PositionalOpponent getPositionalOpponent(Opponent opponent) {
    PositionalOpponent positionalOpponent = null;
    for (PositionalOpponent io : getPositionalOpponents()) {
      if (io.getOpponent().equals(opponent)) {
        positionalOpponent = io;
      }
    }
    return positionalOpponent;
  }

  /**
   * Lesen des indizierten Opponents, bei dem die Position mit dem Parameter
   * \u00fcbereinstimmt.
   *
   * @param reqPosition Index, zu dem der indizierte Opponent gefunden werden soll.
   * @return Der IndexedOpponent
   */
  public PositionalOpponent getPositionalOpponent(Integer reqPosition) {
    PositionalOpponent positionalOpponent = null;
    for (PositionalOpponent po : getPositionalOpponents()) {
      if (po != null && po.getPosition().equals(reqPosition)) {
        positionalOpponent = po;
      }
    }
    return positionalOpponent;
  }

  /**
   * Gibt den Opponents zurueck, der in den entsprechenden Index hat.
   *
   * @param opponentsPosition Index des gesuchten Opopnents.
   * @return a {@link org.cc.torganizer.core.entities.Opponent} object.
   */
  public Opponent getOpponent(Integer opponentsPosition) {
    PositionalOpponent po = getPositionalOpponent(opponentsPosition);
    return po == null ? null : po.getOpponent();
  }

  /**
   * \u00c4ndern der Reihenfolge innerhalb der Liste der indizierten Opponents
   * durch tauschen der Position, so dass der \u00fcbergebene Opponent die
   * n\u00e4chst kleinere Position bekommt.
   *
   * @param opponent Opponent, der in der Positionsreihenfolge weiter vorne stehen
   *                 soll (kleinerer Position)
   */
  public void moveUp(Opponent opponent) {
    PositionalOpponent positionalOpponent = getPositionalOpponent(opponent);
    int positionOld = positionalOpponent.getPosition();

    if (positionOld > 0) {
      PositionalOpponent swap = getPositionalOpponent(positionOld - 1);
      positionalOpponent.swapPosition(swap);
    }
  }

  /**
   * Tauschen der Position dieses Opponents mit dem Opponent, der die um eins
   * hoehere Position hat (wenn moeglich).
   *
   * @param opponent Opponent, der in der Positionsliste um eins nach unten
   *                 wandern soll
   */
  public void moveDown(Opponent opponent) {
    PositionalOpponent positionalOpponent = getPositionalOpponent(opponent);
    int positionOld = positionalOpponent.getPosition();

    if (positionOld <= getPositionalOpponents().size() - 2) {
      PositionalOpponent swap = getPositionalOpponent(positionOld + 1);
      positionalOpponent.swapPosition(swap);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer newPosition) {
    this.position = newPosition;
  }

  /**
   * <p>
   * Getter for the field <code>matches</code>.
   * </p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<Match> getMatches() {
    return matches;
  }

  /**
   * Liste aller laufenden Matches.
   *
   * @return Liste aller laufenden Matches
   */
  public List<Match> getRunningMatches() {
    List<Match> runningMatches = new ArrayList<>();

    for (Match match : getMatches()) {
      if (!match.isFinished()) {
        runningMatches.add(match);
      }
    }

    return runningMatches;
  }

  /**
   * Liste aller abgeschlossenen Matches.
   *
   * @return Liste aller abgeschlossenen Matches
   */
  public List<Match> getFinishedMatches() {
    List<Match> finishedMatches = new ArrayList<>();

    for (Match match : getMatches()) {
      if (match.isFinished()) {
        finishedMatches.add(match);
      }
    }

    return finishedMatches;
  }

  /**
   * <p>
   * Setter for the field <code>matches</code>.
   * </p>
   *
   * @param newMatches a {@link java.util.List} object.
   */
  public void setMatches(List<Match> newMatches) {
    this.matches = newMatches;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "[position:" + position + "]"
      + "[matches:" + getMatches().size() + "]"
      + "[positionalOpponents:" + positionalOpponents.size() + "]";
  }

  /**
   * Match mit der gewuenschten Position. Die Position ist insbesondere fuer das
   * finden der noch ausstehenden Matches von Bedeutung.
   *
   * @param matchPosition Indes des gesuchten Matches
   * @return a {@link org.cc.torganizer.core.entities.Match} object.
   */
  public Match getMatch(Integer matchPosition) {
    for (Match match : getMatches()) {
      if (match != null && matchPosition.equals(match.getPosition())) {
        return match;
      }
    }
    return null;
  }
}
