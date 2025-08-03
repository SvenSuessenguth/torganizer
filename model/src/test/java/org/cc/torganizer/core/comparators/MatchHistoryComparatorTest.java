package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MatchHistoryComparatorTest {

  private MatchHistoryComparator matchHistoryComparator;

  @BeforeEach
  void before() {
    matchHistoryComparator = new MatchHistoryComparator();
  }

  @Test
  void testComparePlayerNull() {

    var now = LocalDateTime.now();

    var p1 = new Player();
    p1.setLastMatchTime(now);
    var p2 = new Player();
    var m1 = new Match(p1, p2);

    var p3 = new Player();
    p3.setLastMatchTime(now);
    var p4 = new Player();
    p4.setLastMatchTime(now);
    var m2 = new Match(p3, p4);

    // m1 < m2
    assertThat(m1).usingComparator(matchHistoryComparator).isNotEqualTo(m2);
  }

  @Test
  void testComparePlayerNotEquals() {

    var p1Ldt = LocalDateTime.of(2000, 12, 24, 0, 0);
    var p2Ldt = LocalDateTime.of(2000, 12, 25, 0, 0);

    var p1 = new Player();
    p1.setLastMatchTime(p1Ldt);
    var p2 = new Player();
    p2.setLastMatchTime(p1Ldt);
    var m1 = new Match(p1, p2);

    var p3 = new Player();
    p3.setLastMatchTime(p2Ldt);
    var p4 = new Player();
    p4.setLastMatchTime(p2Ldt);
    var m2 = new Match(p3, p4);

    // m1 < m2
    assertThat(m1).usingComparator(matchHistoryComparator).isNotEqualTo(m2);
  }
}
