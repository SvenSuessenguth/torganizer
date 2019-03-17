package org.cc.torganizer.core.comparators;

import static org.junit.jupiter.api.Assertions.*;

import org.cc.torganizer.core.entities.Club;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClubComparatorTest {

  private ClubComparator comparator;

  @BeforeEach
  public void beforeEach(){
    comparator = new ClubComparator();
  }

  @AfterEach
  public void after(){
    comparator = null;
  }

  @Test
  public void testCompare_Null_Null(){
    int result = comparator.compare(null, null);
    MatcherAssert.assertThat(result, Matchers.is(0));
  }

  @Test
  public void testCompare_Club_Null(){
    Club c1 = new Club("c1");

    int result = comparator.compare(c1, null);
    MatcherAssert.assertThat(result, Matchers.is(-1));
  }

  @Test
  public void testCompare_Null_Club(){
    Club c1 = new Club("c1");

    int result = comparator.compare(null, c1);
    MatcherAssert.assertThat(result, Matchers.is(1));
  }

  @Test
  public void testCompare_Clubs_with_same_name(){
    Club c1 = new Club("club");
    Club c2 = new Club("club");

    int result = comparator.compare(c1, c2);
    MatcherAssert.assertThat(result, Matchers.is(0));
  }

  @Test
  public void testCompare_Clubs_with_different_name(){
    Club c1 = new Club("club1");
    Club c2 = new Club("club2");

    int result = comparator.compare(c1, c2);
    MatcherAssert.assertThat(result, Matchers.is(-1));
  }
}