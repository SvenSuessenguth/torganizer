package org.cc.torganizer.core.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpponentByNameComparatorTest {

  private OpponentByNameComparator comparator;

  @BeforeEach
  void before() {
    comparator = new OpponentByNameComparator();
  }

  @Test
  void compare_bothNull() {
    int c = comparator.compare(null, null);

    assertThat(c).isEqualTo(0);
  }

  @Test
  void compare_bothOpponentsHasNoPlayers() {
    Squad s1 = new Squad();
    Squad s2 = new Squad();

    int c = comparator.compare(s1, s2);

    assertThat(c).isEqualTo(0);
  }

  @Test
  void compare_firstOpponentHasNoPlayers() {
    Squad s1 = new Squad();
    Squad s2 = new Squad();
    s2.addPlayer(new Player(new Person("s2", "s2")));

    int c = comparator.compare(s1, s2);

    assertThat(c).isGreaterThan(0);
  }

  @Test
  void compare_secondOpponentHasNoPlayers() {
    Squad s1 = new Squad();
    Squad s2 = new Squad();
    s1.addPlayer(new Player(new Person("s1", "s1")));

    int c = comparator.compare(s1, s2);

    assertThat(c).isLessThan(0);
  }

  @Test
  void compare_firstIsSmaller() {
    Squad s1 = new Squad();
    s1.addPlayer(new Player(new Person("a", "a")));
    Squad s2 = new Squad();
    s2.addPlayer(new Player(new Person("bb", "b")));

    int c = comparator.compare(s1, s2);

    assertThat(c).isEqualTo(-1);
  }

  @Test
  void compare_secondIsSmaller() {
    Squad s1 = new Squad();
    s1.addPlayer(new Player(new Person("b", "b")));
    Squad s2 = new Squad();
    s2.addPlayer(new Player(new Person("a", "a")));

    int c = comparator.compare(s1, s2);

    assertThat(c).isEqualTo(1);
  }

  @Test
  void compare_firstIsNull() {
    int actual = comparator.compare(null, new Squad());

    assertThat(actual).isGreaterThan(0);
  }

  @Test
  void compare_secondIsNull() {
    int actual = comparator.compare(new Squad(), null);

    assertThat(actual).isLessThan(0);
  }
}