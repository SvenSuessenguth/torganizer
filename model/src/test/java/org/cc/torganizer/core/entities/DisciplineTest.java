package org.cc.torganizer.core.entities;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.of;
import static java.time.Month.*;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.AGE_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.GENDER_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class DisciplineTest {

  private Discipline discipline;

  @Before
  public void before(){
    discipline = new Discipline();
    discipline.addRestriction(new AgeRestriction(of(1973, MAY, 5), of(1968, JANUARY, 1)));
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

  @Test
  public void testIsAssignable_true(){
    // opponents
    Player player = new Player(new Person("A", "A", LocalDate.of(1970, JANUARY, 1), MALE));

    assertThat(discipline.isAssignable(player), is(true));
  }

  @Test
  public void testIsAssignable_stream(){
    // opponents
    List<Opponent> opponents = new ArrayList<>();
    opponents.add(new Player(new Person("A", "A", LocalDate.of(1970, JANUARY, 1), MALE)));
    opponents.add(new Player(new Person("B", "B", LocalDate.of(1970, JANUARY, 1), MALE)));
    opponents.add(new Player(new Person("C", "C", LocalDate.of(1970, JANUARY, 1), MALE)));
    opponents.add(new Player(new Person("D", "D", LocalDate.of(1970, JANUARY, 1), MALE)));

    opponents.add(new Player(new Person("D", "D", LocalDate.of(1970, JANUARY, 1), FEMALE)));
    opponents.add(new Player(new Person("D", "D", LocalDate.of(2000, JANUARY, 1), MALE)));
    opponents.add(new Squad());


    List<Opponent> assignableOpponents = opponents.stream().filter(opponent -> discipline.isAssignable(opponent)).collect(Collectors.toList());

    assertThat(assignableOpponents, hasSize(4));
  }
}