package org.cc.torganizer.core.entities;

import static java.time.LocalDate.of;
import static java.time.Month.JANUARY;
import static java.time.Month.MAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.AGE_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.GENDER_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisciplineTest {

  private Discipline discipline;

  @BeforeEach
  void before() {
    discipline = new Discipline();
    discipline.addRestriction(new AgeRestriction(of(1973, MAY, 5), of(1968, JANUARY, 1)));
    discipline.addRestriction(new GenderRestriction(MALE));
    discipline.addRestriction(new OpponentTypeRestriction(PLAYER));
  }

  @Test
  void testGetRestriction_null() {
    Restriction restriction = discipline.getRestriction(null);
    assertThat(restriction).isNull();
  }

  @Test
  void testGetRestriction_ageRestriction() {
    Restriction restriction = discipline.getRestriction(AGE_RESTRICTION);

    assertThat(restriction).isNotNull();
    assertThat(restriction.getDiscriminator()).isEqualTo(AGE_RESTRICTION);
  }

  @Test
  void testGetRestriction_genderRestriction() {
    Restriction restriction = discipline.getRestriction(GENDER_RESTRICTION);

    assertThat(restriction).isNotNull();
    assertThat(restriction.getDiscriminator()).isEqualTo(GENDER_RESTRICTION);
  }

  @Test
  void testGetRestriction_opponentTypeRestriction() {
    Restriction restriction = discipline.getRestriction(OPPONENT_TYPE_RESTRICTION);

    assertThat(restriction).isNotNull();
    assertThat(restriction.getDiscriminator()).isEqualTo(OPPONENT_TYPE_RESTRICTION);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Test
  void testGetRestrictions_order() {
    Discipline discipline = new Discipline();
    discipline.addRestriction(new OpponentTypeRestriction());
    discipline.addRestriction(new AgeRestriction());
    discipline.addRestriction(new GenderRestriction());

    List<Restriction> restrictions = new ArrayList(discipline.getRestrictions());
    assertThat(restrictions.get(0)).isInstanceOf(AgeRestriction.class);
    assertThat(restrictions.get(1)).isInstanceOf(GenderRestriction.class);
    assertThat(restrictions.get(2)).isInstanceOf(OpponentTypeRestriction.class);
  }

  @Test
  void testSetRounds_empty() {
    List<Round> rounds = new ArrayList<>();
    Discipline discipline = new Discipline();
    discipline.addRound(new Round());
    discipline.setRounds(rounds);

    assertThat(discipline.getRounds()).isEmpty();
  }

  @Test
  void testSetRounds_null() {
    Discipline discipline = new Discipline();
    discipline.setRounds(null);

    assertThat(discipline.getRounds()).isNotNull();
  }

  @Test
  void testSetRounds_some() {
    List<Round> rounds = new ArrayList<>();
    rounds.add(new Round());
    Discipline discipline = new Discipline();
    discipline.setRounds(rounds);

    assertThat(discipline.getRounds()).hasSize(1);
  }
}