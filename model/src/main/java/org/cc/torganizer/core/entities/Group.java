package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.comparators.AggregationComparator;
import org.cc.torganizer.core.comparators.PositionalComparator;
import org.cc.torganizer.core.entities.aggregates.Aggregation;
import org.cc.torganizer.core.entities.aggregates.MatchAggregate;
import org.cc.torganizer.core.entities.aggregates.ResultAggregate;
import org.cc.torganizer.core.entities.aggregates.ScoreAggregate;

/**
 * Eine Group sammelt Opponents zusammen. Innerhalb einer Group werden die
 * Matches ausgef\u00fchrt, die den Sieger oder die Opponents, die in die
 * n\u00e4chste Round kommen, bestimmen
 */
public class Group extends Entity implements Positional {

  private Integer position;

  private List<PositionalOpponent> positionalOpponents = new ArrayList<>();

  /**
   * Ein Match soll nur der Group zugewiesen werden, wenn es persistiert werden
   * soll (running/finished).
   */
  private List<Match> matches = new ArrayList<>();

  public Group() {
  }

  public Group(Integer position) {
    this.position = position;
  }

  /**
   * Gibt die nach Position sortierte Liste von Opponents zur\u00fcck.
   *
   * @return nach Position sortierte Liste von PositionalOpponents
   */
  public List<PositionalOpponent> getPositionalOpponents() {
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
    positionalOpponents.sort(new PositionalComparator());
  }

  /**
   * Lesen der Opponents, die dieser Group zugewiesen sind. Die Liste
   * enth\u00e4lt die Opponents in der durch die Position  vorgegebenen Reihenfolge.
   *
   * @return Liste der Opponents, die dieser Gruppe zugewiesen sind.
   */
  public List<Opponent> getOpponents() {
    var result = new ArrayList<Opponent>();
    getPositionalOpponents().sort(new PositionalComparator());

    for (var io : getPositionalOpponents()) {
      result.add(io.getOpponent());
    }

    return result;
  }

  /**
   * Liste der Opponents, die zu dieser Group geh\u00f6ren wird neu gesetzt.
   * Alte Inhalte werden dabei gel\u00f6scht.
   *
   * @param newOpponents Opponents, die der Group zugewiesen werden sollen.
   */
  public void setOpponents(List<Opponent> newOpponents) {
    getPositionalOpponents().clear();
    if (newOpponents == null || newOpponents.isEmpty()) {
      return;
    }

    for (var o : newOpponents) {
      var io = new PositionalOpponent(o, newOpponents.indexOf(o) + 1);
      getPositionalOpponents().add(io);
    }
  }

  /**
   * Hinzuf\u00fcgen eines (unindizierten) Opponents. Die neue Position ist so
   * gro\u00df, dass der neue Opponent als letzter angef\u00fcgt wird.
   *
   * @param opponent Opponent, der de Group hinzugef\u00fcgt werden soll.
   */
  public void addOpponent(Opponent opponent) {
    Integer lastPosition = positionalOpponents.size();
    var positionalOpponent = new PositionalOpponent();
    positionalOpponent.setPosition(lastPosition);
    positionalOpponent.setOpponent(opponent);

    getPositionalOpponents().add(positionalOpponent);
    positionalOpponents.sort(new PositionalComparator());
  }

  /**
   * Entfernen eines Opponents aus der Liste aller (indizierten) Opponents.
   *
   * @param opponent Opponent, der entfernt werden soll.
   */
  public void removeOpponent(Opponent opponent) {

    // passenden IndexedOpponent finden und entfernen
    var poToRemove = getPositionalOpponent(opponent);
    getPositionalOpponents().remove(poToRemove);

    // Neuindizierung
    var newPosition = 0;
    for (var o : getPositionalOpponents()) {
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
    for (var io : getPositionalOpponents()) {
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
    for (var po : getPositionalOpponents()) {
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
    var po = getPositionalOpponent(opponentsPosition);
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
    var positionalOpponent = getPositionalOpponent(opponent);
    var positionOld = positionalOpponent.getPosition();

    if (positionOld > 0) {
      var swap = getPositionalOpponent(positionOld - 1);
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
    var positionalOpponent = getPositionalOpponent(opponent);
    var positionOld = positionalOpponent.getPosition();

    if (positionOld <= getPositionalOpponents().size() - 2) {
      var swap = getPositionalOpponent(positionOld + 1);
      positionalOpponent.swapPosition(swap);
    }
  }

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
   * <p>
   * Setter for the field <code>matches</code>.
   * </p>
   *
   * @param newMatches a {@link java.util.List} object.
   */
  public void setMatches(List<Match> newMatches) {
    this.matches = newMatches == null ? List.of() : newMatches;
  }

  /**
   * Liste aller laufenden Matches.
   *
   * @return Liste aller laufenden Matches
   */
  public List<Match> getRunningMatches() {
    return getMatches()
        .stream()
        .filter(m -> m.isRunning() && !m.isFinished())
        .toList();
  }

  /**
   * Liste aller abgeschlossenen Matches.
   *
   * @return Liste aller abgeschlossenen Matches
   */
  public List<Match> getFinishedMatches() {
    return getMatches()
        .stream()
        .filter(Match::isFinished).toList();
  }

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
    for (var match : getMatches()) {
      if (match != null && matchPosition.equals(match.getPosition())) {
        return match;
      }
    }
    return null;
  }

  /**
   * Aggregating all matches of all opponents of this group to get a ranking.
   */
  public List<Aggregation> getAggregates() {
    var aggregates = new ArrayList<Aggregation>();

    // zusammenstellen der Aggregates
    for (var opponent : getOpponents()) {
      var ma = new MatchAggregate();
      var ra = new ResultAggregate();
      var sa = new ScoreAggregate();

      for (var match : getMatches()) {
        ma.aggregate(match, opponent);
        ra.aggregate(match, opponent);
        sa.aggregate(match, opponent);
      }

      var ca = new Aggregation();
      ca.setOpponent(opponent);
      ca.setMa(ma);
      ca.setRa(ra);
      ca.setSa(sa);

      aggregates.add(ca);
    }

    // sortieren
    aggregates.sort(new AggregationComparator().reversed());

    return aggregates;
  }
}
