package org.cc.torganizer.core.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import org.cc.torganizer.core.entities.Club;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    int result = comparator.compare(null, null);

    assertThat(result).isEqualTo(0);
  }

  @Test
  void testCompare_Club_Null() {
    Club c1 = new Club("c1");

    int result = comparator.compare(c1, null);

    assertThat(result).isEqualTo(-1);
  }

  @Test
  void testCompare_Null_Club() {
    Club c1 = new Club("c1");

    int result = comparator.compare(null, c1);

    assertThat(result).isEqualTo(1);
  }

  @Test
  void testCompare_Clubs_with_same_name() {
    Club c1 = new Club("club");
    Club c2 = new Club("club");

    int result = comparator.compare(c1, c2);

    assertThat(result).isEqualTo(0);
  }

  @Test
  void testCompare_Clubs_with_different_name() {
    Club c1 = new Club("club1");
    Club c2 = new Club("club2");

    int result = comparator.compare(c1, c2);

    assertThat(result).isEqualTo(-1);
  }
}