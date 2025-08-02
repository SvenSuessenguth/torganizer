package org.cc.torganizer.core.entities.aggregates;

import lombok.Data;
import org.cc.torganizer.core.entities.Opponent;

/**
 * Zusammenfassung aller aggregierten Daten eines Opponents ueber alle Matches,
 * Results (entsprechen Saetzen) und den Scores.
 */
@Data
public class Aggregation {
  private Opponent opponent;
  private MatchAggregate matchAggregate;
  private ResultAggregate resultAggregate;
  private ScoreAggregate scoreAggregate;
}
