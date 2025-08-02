package org.cc.torganizer.core.comparators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cc.torganizer.core.entities.aggregates.Aggregation;
import org.cc.torganizer.core.entities.aggregates.MatchAggregate;
import org.cc.torganizer.core.entities.aggregates.ResultAggregate;
import org.cc.torganizer.core.entities.aggregates.ScoreAggregate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AggregationComparatorTest {

  private AggregationComparator comparator;

  @BeforeEach
  public void before() {
    comparator = new AggregationComparator();
  }


  @Test
  void testIdenticalSame() {
    Aggregation a1 = new Aggregation();
    assertThat(a1).usingComparator(comparator).isEqualTo(a1);
  }

  @Test
  void testEqualsMatchesOnly() {
    MatchAggregate ma1 = mock(MatchAggregate.class);
    when(ma1.getWins()).thenReturn(1);
    MatchAggregate ma2 = mock(MatchAggregate.class);
    when(ma2.getWins()).thenReturn(1);

    Aggregation a1 = new Aggregation();
    a1.setMatchAggregate(ma1);

    Aggregation a2 = new Aggregation();
    a2.setMatchAggregate(ma2);

    assertThat(a1).usingComparator(comparator).isEqualTo(a2);
  }

  @Test
  void testEqualsResultsOnly() {
    ResultAggregate ra1 = mock(ResultAggregate.class);
    when(ra1.getWins()).thenReturn(1);
    ResultAggregate ra2 = mock(ResultAggregate.class);
    when(ra2.getWins()).thenReturn(1);

    Aggregation a1 = new Aggregation();
    a1.setResultAggregate(ra1);

    Aggregation a2 = new Aggregation();
    a2.setResultAggregate(ra2);

    assertThat(a1).usingComparator(comparator).isEqualTo(a2);
  }

  @Test
  void testEqualsScoresOnly() {
    ScoreAggregate sa1 = mock(ScoreAggregate.class, RETURNS_DEEP_STUBS);
    when(sa1.getWins()).thenReturn(1);
    ScoreAggregate sa2 = mock(ScoreAggregate.class);
    when(sa2.getWins()).thenReturn(1);

    Aggregation a1 = new Aggregation();
    a1.setScoreAggregate(sa1);

    Aggregation a2 = new Aggregation();
    a2.setScoreAggregate(sa2);

    assertThat(a1).usingComparator(comparator).isEqualTo(a2);
  }

  @Test
  void testO1IsNull() {
    Aggregation aggregation = new Aggregation();

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() ->
            comparator.compare(null, aggregation));
  }

  @Test
  void testO2IsNull() {
    Aggregation aggregation = new Aggregation();

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() ->
            comparator.compare(aggregation, null));
  }

  @Test
  void testBothAreNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() ->
            comparator.compare(null, null));
  }
}
