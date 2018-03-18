package org.cc.torganizer.core.entities;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static java.time.LocalDate.of;
import static java.time.Month.*;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.AGE_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.GENDER_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class DisciplineTest {

  private Discipline discipline;

  @Before
  public void before(){
    discipline = new Discipline();
    discipline.addRestriction(new AgeRestriction(of(1968, JANUARY, 1), of(1973, MAY, 5)));
    discipline.addRestriction(new GenderRestriction(MALE));
    discipline.addRestriction(new OpponentTypeRestriction(PLAYER));
  }

  @Test
  public void testGetRestriction_null() {
    Restriction restriction = discipline.getRestriction(null);
    MatcherAssert.assertThat(restriction, is(nullValue()));
  }

  @Test
  public void testGetRestriction_ageRestriction(){
    Restriction restriction = discipline.getRestriction(AGE_RESTRICTION);

    MatcherAssert.assertThat(restriction, is(not(nullValue())));
    MatcherAssert.assertThat(restriction.getDiscriminator(), is(AGE_RESTRICTION));
  }

  @Test
  public void testGetRestriction_genderRestriction(){
    Restriction restriction = discipline.getRestriction(GENDER_RESTRICTION);

    MatcherAssert.assertThat(restriction, is(not(nullValue())));
    MatcherAssert.assertThat(restriction.getDiscriminator(), is(GENDER_RESTRICTION));
  }

  @Test
  public void testGetRestriction_opponentTypeRestriction(){
    Restriction restriction = discipline.getRestriction(OPPONENT_TYPE_RESTRICTION);

    MatcherAssert.assertThat(restriction, is(not(nullValue())));
    MatcherAssert.assertThat(restriction.getDiscriminator(), is(OPPONENT_TYPE_RESTRICTION));
  }
}