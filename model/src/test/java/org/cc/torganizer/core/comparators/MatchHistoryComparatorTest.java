package org.cc.torganizer.core.comparators;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author svens
 */
public class MatchHistoryComparatorTest {

  private MatchHistoryComparator matchHistoryComparator;

  @BeforeEach
  public void before() {
    matchHistoryComparator = new MatchHistoryComparator();
  }

//  @Test
//  public void testComparePlayerNull() {
//
//    Calendar calendar = new GregorianCalendar(Locale.GERMAN);
//    calendar.set(2000, Calendar.DECEMBER, 24, 0, 0);
//
//    Player p1 = new Player();
//    p1.setLastMatch(calendar);
//    Player p2 = new Player();
//    Match m1 = new Match(null, p1, p2);
//
//    Player p3 = new Player();
//    p3.setLastMatch(calendar);
//    Player p4 = new Player();
//    p4.setLastMatch(calendar);
//    Match m2 = new Match(null, p3, p4);
//
//    assertTrue(matchHistoryComparator.compare(m1, m2) < 0);
//  }
//
//  @Test
//  public void testComparePlayerNotEquals() {
//
//    Calendar calendar = new GregorianCalendar(Locale.GERMAN);
//    calendar.set(2000, Calendar.DECEMBER, 24, 0, 0);
//    Calendar later = new GregorianCalendar(Locale.GERMAN);
//    later.set(2000, Calendar.DECEMBER, 25, 0, 0);
//
//    Player p1 = new Player();
//    p1.setLastMatch(calendar);
//    Player p2 = new Player();
//    p2.setLastMatch(calendar);
//    Match m1 = new Match(null, p1, p2);
//
//    Player p3 = new Player();
//    p3.setLastMatch(later);
//    Player p4 = new Player();
//    p4.setLastMatch(later);
//    Match m2 = new Match(null, p3, p4);
//
//    assertTrue(matchHistoryComparator.compare(m1, m2) < 0);
//  }
}
