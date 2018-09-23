package org.cc.torganizer.core.entities;

import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.of;
import static java.time.Month.JANUARY;
import static java.time.Month.MAY;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DisciplineTest {

  private Discipline discipline;

  @BeforeEach
  public void before(){
    discipline = new Discipline();
    discipline.addRestriction(new AgeRestriction(of(1973, MAY, 5), of(1968, JANUARY, 1)));
    discipline.addRestriction(new GenderRestriction(MALE));
    discipline.addRestriction(new OpponentTypeRestriction(PLAYER));
  }

  @Test
  public void testGetRestriction_null() {
    Restriction restriction = discipline.getRestriction(null);
    assertThat(restriction, is(nullValue()));
  }

  @Test
  public void testGetRestriction_ageRestriction(){
    Restriction restriction = discipline.getRestriction(AGE_RESTRICTION);

    assertThat(restriction, is(not(nullValue())));
    assertThat(restriction.getDiscriminator(), is(AGE_RESTRICTION));
  }

  @Test
  public void testGetRestriction_genderRestriction(){
    Restriction restriction = discipline.getRestriction(GENDER_RESTRICTION);

    assertThat(restriction, is(not(nullValue())));
    assertThat(restriction.getDiscriminator(), is(GENDER_RESTRICTION));
  }

  @Test
  public void testGetRestriction_opponentTypeRestriction(){
    Restriction restriction = discipline.getRestriction(OPPONENT_TYPE_RESTRICTION);

    assertThat(restriction, is(not(nullValue())));
    assertThat(restriction.getDiscriminator(), is(OPPONENT_TYPE_RESTRICTION));
  }

  @Test
  public void testGetRestrictions_order(){
    Discipline discipline = new Discipline();
    discipline.addRestriction(new OpponentTypeRestriction());
    discipline.addRestriction(new AgeRestriction());
    discipline.addRestriction(new GenderRestriction());

    List<Restriction> restrictions = new ArrayList(discipline.getRestrictions());
    assertThat(restrictions.get(0), is(instanceOf(AgeRestriction.class)));
    assertThat(restrictions.get(1), is(instanceOf(GenderRestriction.class)));
    assertThat(restrictions.get(2), is(instanceOf(OpponentTypeRestriction.class)));
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

    opponents.add(new Player(new Person("E", "D", LocalDate.of(1970, JANUARY, 1), FEMALE)));
    opponents.add(new Player(new Person("F", "D", LocalDate.of(2000, JANUARY, 1), MALE)));
    opponents.add(new Squad());

    List<Opponent> assignableOpponents = opponents.stream().filter(opponent -> discipline.isAssignable(opponent)).collect(Collectors.toList());
    assertThat(assignableOpponents, hasSize(4));
  }

  @Test
  public void testSetRounds_empty(){
    List<Round> rounds = new ArrayList<>();
    Discipline discipline = new Discipline();
    discipline.addRound(new Round());
    discipline.setRounds(rounds);

    assertThat(discipline.getRounds(), is(empty()));
  }

  @Test
  public void testSetRounds_null(){
    List<Round> rounds = null;
    Discipline discipline = new Discipline();
    discipline.setRounds(rounds);

    assertThat(discipline.getRounds(), is(not(nullValue())));
  }

  @Test
  public void testSetRounds_some(){
    List<Round> rounds = new ArrayList<>();
    rounds.add(new Round());
    Discipline discipline = new Discipline();
    discipline.setRounds(rounds);

    assertThat(discipline.getRounds(), hasSize(1));
  }
}