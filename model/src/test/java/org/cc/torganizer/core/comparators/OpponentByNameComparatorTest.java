package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class OpponentByNameComparatorTest {

  private OpponentByNameComparator comparator;

  @Before
  public void before(){
    comparator = new OpponentByNameComparator();
  }

  @Test
  public void compare_bothNull() {
    int c = comparator.compare(null, null);

    assertThat(c, is(0));
  }

  @Test
  public void compare_bothOpponentsHasNoPlayers(){
    Squad s1 = new Squad();
    Squad s2 = new Squad();

    int c = comparator.compare(s1, s2);

    assertThat(c, is(0));
  }

  @Test
  public void compare_firstOpponentHasNoPlayers(){
    Squad s1 = new Squad();
    Squad s2 = new Squad();
    s2.addPlayer(new Player(new Person("s2", "s2")));

    int c = comparator.compare(s1, s2);

    assertThat(c, is(-1));
  }

  @Test
  public void compare_secondOpponentHasNoPlayers(){
    Squad s1 = new Squad();
    Squad s2 = new Squad();
    s1.addPlayer(new Player(new Person("s1", "s1")));

    int c = comparator.compare(s1, s2);

    assertThat(c, is(1));
  }

  @Test
  public void compare_firstIsSmaller(){
    Squad s1 = new Squad();
    s1.addPlayer(new Player(new Person("a", "a")));
    Squad s2 = new Squad();
    s2.addPlayer(new Player(new Person("bb", "b")));

    int c = comparator.compare(s1, s2);

    assertThat(c, is(-1));
  }

  @Test
  public void compare_secondIsSmaller(){
    Squad s1 = new Squad();
    s1.addPlayer(new Player(new Person("b", "b")));
    Squad s2 = new Squad();
    s2.addPlayer(new Player(new Person("a", "a")));

    int c = comparator.compare(s1, s2);

    assertThat(c, is(1));
  }
}