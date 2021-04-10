package org.cc.torganizer.core.roundrobin;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.comparators.AggregationComparator;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.aggregates.Aggregation;
import org.cc.torganizer.core.entities.aggregates.MatchAggregate;
import org.cc.torganizer.core.entities.aggregates.ResultAggregate;
import org.cc.torganizer.core.entities.aggregates.ScoreAggregate;

@RequestScoped
@Named
public final class RoundRobinFunctions {

  @SuppressWarnings("unchecked")
  public List<Aggregation> getAggregates(Group group) {
    List<Aggregation> aggregates = new ArrayList<Aggregation>();
    if (group == null) {
      return aggregates;
    }

    // zusammenstellen der Aggregates
    for (Opponent opponent : group.getOpponents()) {
      MatchAggregate ma = new MatchAggregate();
      ResultAggregate ra = new ResultAggregate();
      ScoreAggregate sa = new ScoreAggregate();

      for (Match match : group.getMatches()) {
        ma.aggregate(match, opponent);
        ra.aggregate(match, opponent);
        sa.aggregate(match, opponent);
      }

      Aggregation ca = new Aggregation();
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