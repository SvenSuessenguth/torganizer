package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class PlayerByNameComparatorTest {

  private PlayerByNameComparator comparator;

  @Before
  public void setUp() throws Exception {
    comparator = new PlayerByNameComparator();
  }

  @Test
  public void compare_bothNull() {
    int c = comparator.compare(null, null);

    assertThat(c, is(0));
  }

  @Test
  public void compare_bothEquals(){
    Player p = new Player(new Person("A", "B"));
    int c = comparator.compare(p, p);

    assertThat(c, is(0));
  }

  @Test
  public void compare_bothSameLastName(){
    Player p1 = new Player(new Person("A", "B"));
    Player p2 = new Player(new Person("B", "B"));
    int c = comparator.compare(p1, p2);

    assertThat(c, is(0));
  }

  @Test
  public void compare_firstIsNull(){
    Player p2 = new Player(new Person("B", "B"));
    int c = comparator.compare(null, p2);

    assertThat(c, is(-1));
  }

  @Test
  public void compare_secondIsNull(){
    Player p1 = new Player(new Person("A", "B"));
    int c = comparator.compare(p1, null);

    assertThat(c, is(1));
  }

  @Test
  public void compare_lowerAndUpperCase(){
    Player p1 = new Player(new Person("A", "B"));
    Player p2 = new Player(new Person("a", "b"));
    int c = comparator.compare(p1, p2);

    assertThat(c, is(0));
  }
}