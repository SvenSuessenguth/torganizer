package org.cc.torganizer.core.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchHistoryComparatorTest {

  private MatchHistoryComparator matchHistoryComparator;

  @BeforeEach
  public void before() {
    matchHistoryComparator = new MatchHistoryComparator();
  }

  @Test
  void testComparePlayerNull() {

    LocalDateTime now = LocalDateTime.now();

    Player p1 = new Player();
    p1.setLastMatchTime(now);
    Player p2 = new Player();
    Match m1 = new Match(p1, p2);

    Player p3 = new Player();
    p3.setLastMatchTime(now);
    Player p4 = new Player();
    p4.setLastMatchTime(now);
    Match m2 = new Match(p3, p4);

    // m1 < m2
    assertThat(m1).usingComparator(matchHistoryComparator).isNotEqualTo(m2);
  }

  @Test
  void testComparePlayerNotEquals() {

    LocalDateTime p1Ldt = LocalDateTime.of(2000, 12, 24, 0, 0);
    LocalDateTime p2Ldt = LocalDateTime.of(2000, 12, 25, 0, 0);

    Player p1 = new Player();
    p1.setLastMatchTime(p1Ldt);
    Player p2 = new Player();
    p2.setLastMatchTime(p1Ldt);
    Match m1 = new Match(p1, p2);

    Player p3 = new Player();
    p3.setLastMatchTime(p2Ldt);
    Player p4 = new Player();
    p4.setLastMatchTime(p2Ldt);
    Match m2 = new Match(p3, p4);

    // m1 < m2
    assertThat(m1).usingComparator(matchHistoryComparator).isNotEqualTo(m2);
  }
}
