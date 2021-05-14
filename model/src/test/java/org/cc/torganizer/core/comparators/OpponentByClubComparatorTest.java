package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.Bye;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author svens
 */
public class OpponentByClubComparatorTest {

  private OpponentsByClubComparator comparator;

  @BeforeEach
  public void before() {
    comparator = new OpponentsByClubComparator();
  }

//  @Test
//  public void testComparePlayersNoClubs() {
//    Opponent player0 = new Player(new Person());
//    Opponent player1 = new Player(new Person());
//
//    assertEquals(0, comparator.compare(player0, player1));
//  }
//
//  @Test
//  public void testComparePlayersNoClub() {
//    Player player0 = new Player(new Person());
//    player0.setClub(new Club("club-1"));
//    Opponent player1 = new Player(new Person());
//
//    assertTrue(comparator.compare(player0, player1) > 0);
//    assertTrue(comparator.compare(player1, player0) < 0);
//  }
//
//  @Test
//  public void testComparePlayersClubs() {
//    Player player0 = new Player(new Person());
//    player0.setClub(new Club("club-1"));
//    Player player1 = new Player(new Person());
//    player1.setClub(new Club("club-2"));
//
//    // club-1 < club-2
//    assertTrue(comparator.compare(player0, player1) < 0);
//    assertTrue(comparator.compare(player1, player0) > 0);
//  }
//
//  @Test
//  public void testComparePlayersBye() {
//    Player player0 = new Player(new Person());
//    player0.setClub(new Club("club-1"));
//    Bye bye = new Bye();
//
//    // bye (ohne Club) < club-1
//    assertTrue(comparator.compare(player0, bye) > 0);
//    assertTrue(comparator.compare(bye, player0) < 0);
//  }
}
