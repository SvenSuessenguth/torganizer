package org.cc.torganizer.core.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerByNameComparatorTest {

  private PlayerByNameComparator comparator;

  @BeforeEach
  public void setUp() throws Exception {
    comparator = new PlayerByNameComparator();
  }

  @Test
  public void compare_bothNull() {
    int c = comparator.compare(null, null);

    assertThat(c).isEqualTo(0);
  }

  @Test
  public void compare_bothEquals(){
    Player p = new Player(new Person("A", "B"));
    int c = comparator.compare(p, p);

    assertThat(c).isEqualTo(0);
  }

  @Test
  public void compare_bothSameLastName(){
    Player p1 = new Player(new Person("A", "B"));
    Player p2 = new Player(new Person("B", "B"));
    int c = comparator.compare(p1, p2);

    assertThat(c).isEqualTo(0);
  }

  @Test
  public void compare_firstIsNull(){
    Player p2 = new Player(new Person("B", "B"));
    int c = comparator.compare(null, p2);

    assertThat(c).isEqualTo(-1);
  }

  @Test
  public void compare_secondIsNull(){
    Player p1 = new Player(new Person("A", "B"));
    int c = comparator.compare(p1, null);

    assertThat(c).isEqualTo(1);
  }

  @Test
  public void compare_lowerAndUpperCase(){
    Player p1 = new Player(new Person("A", "B"));
    Player p2 = new Player(new Person("a", "b"));
    int c = comparator.compare(p1, p2);

    assertThat(c).isEqualTo(0);
  }
}