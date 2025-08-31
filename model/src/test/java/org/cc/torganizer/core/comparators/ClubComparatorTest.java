package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.Club;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClubComparatorTest {

  private ClubComparator comparator;

  @BeforeEach
  void beforeEach() {
    comparator = new ClubComparator();
  }

  @AfterEach
  void after() {
    comparator = null;
  }

  @Test
  void testCompare_Null_Null() {
    var result = comparator.compare(null, null);

    assertThat(result).isZero();
  }

  @Test
  void testCompare_Club_Null() {
    var c1 = new Club("c1");

    var result = comparator.compare(c1, null);

    assertThat(result).isEqualTo(-1);
  }

  @Test
  void testCompare_Null_Club() {
    var c1 = new Club("c1");

    var result = comparator.compare(null, c1);

    assertThat(result).isEqualTo(1);
  }

  @Test
  void testCompare_Clubs_with_same_name() {
    var c1 = new Club("club");
    var c2 = new Club("club");

    var result = comparator.compare(c1, c2);

    assertThat(result).isZero();
  }

  @Test
  void testCompare_Clubs_with_different_name() {
    var c1 = new Club("club1");
    var c2 = new Club("club2");

    var result = comparator.compare(c1, c2);

    assertThat(result).isEqualTo(-1);
  }
}