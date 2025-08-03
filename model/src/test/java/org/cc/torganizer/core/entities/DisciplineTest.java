package org.cc.torganizer.core.entities;

import org.cc.torganizer.core.exceptions.RestrictionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.LocalDate.of;
import static java.time.Month.JANUARY;
import static java.time.Month.MAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.OpponentType.UNKNOWN;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.AGE_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.GENDER_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;

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
    var restriction = discipline.getRestriction(null);
    assertThat(restriction).isNull();
  }

  @Test
  void testGetRestriction_ageRestriction() {
    var restriction = discipline.getRestriction(AGE_RESTRICTION);

    assertThat(restriction).isNotNull();
    assertThat(restriction.getDiscriminator()).isEqualTo(AGE_RESTRICTION);
  }

  @Test
  void testGetRestriction_genderRestriction() {
    var restriction = discipline.getRestriction(GENDER_RESTRICTION);

    assertThat(restriction).isNotNull();
    assertThat(restriction.getDiscriminator()).isEqualTo(GENDER_RESTRICTION);
  }

  @Test
  void testGetRestriction_opponentTypeRestriction() {
    var restriction = discipline.getRestriction(OPPONENT_TYPE_RESTRICTION);

    assertThat(restriction).isNotNull();
    assertThat(restriction.getDiscriminator()).isEqualTo(OPPONENT_TYPE_RESTRICTION);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Test
  void testGetRestrictions_order() {
    // test ordering, so a test-local discipline is used
    var localDiscipline = new Discipline();
    localDiscipline.addRestriction(new OpponentTypeRestriction());
    localDiscipline.addRestriction(new AgeRestriction());
    localDiscipline.addRestriction(new GenderRestriction());

    var restrictions = new ArrayList(localDiscipline.getRestrictions());
    assertThat(restrictions.getFirst()).isInstanceOf(AgeRestriction.class);
    assertThat(restrictions.get(1)).isInstanceOf(GenderRestriction.class);
    assertThat(restrictions.get(2)).isInstanceOf(OpponentTypeRestriction.class);
  }

  @Test
  void testSetRounds_empty() {
    var rounds = new ArrayList<Round>();
    discipline.addRound(new Round());
    discipline.setRounds(rounds);

    assertThat(discipline.getRounds()).isEmpty();
  }

  @Test
  void testSetRounds_null() {
    discipline.setRounds(null);

    assertThat(discipline.getRounds()).isNotNull();
  }

  @Test
  void testSetRounds_some() {
    var rounds = new ArrayList<Round>();
    rounds.add(new Round());
    discipline.setRounds(rounds);

    assertThat(discipline.getRounds()).hasSize(1);
  }

  @Test
  void testGetLastRound_noRoundAvailable() {
    var actual = discipline.getLastRound();
    assertThat(actual).isNull();
  }

  @Test
  void testGetLastRound_roundsAvailable() {
    discipline.addRound(new Round());
    discipline.addRound(new Round());
    discipline.addRound(new Round());
    var actual = discipline.getLastRound();
    assertThat(actual).isNotNull();
    assertThat(actual.getPosition()).isEqualTo(2);
  }

  @Test
  void addOpponent_restriction() {
    var player = new Player("a", "a");
    player.getPerson().setGender(FEMALE);

    assertThatThrownBy(() -> discipline.addOpponent(player)).isInstanceOf(RestrictionException.class);
  }

  @Test
  void addOpponent() {
    var player = new Player("a", "a");
    player.getPerson().setGender(MALE);

    var countBefore = discipline.getOpponents().size();
    discipline.addOpponent(player);
    var countAfter = discipline.getOpponents().size();

    assertThat(countBefore + 1).isEqualTo(countAfter);
  }

  @Test
  void getPlayers() {
    discipline.addRestriction(new OpponentTypeRestriction(UNKNOWN));

    var squad = new Squad();
    var p1 = Person.builder()
      .firstName("p1")
      .dateOfBirth(LocalDate.of(1968, JANUARY, 12 ))
      .gender(MALE)
      .build();
    var p2 = Person.builder()
      .firstName("p2")
      .dateOfBirth(LocalDate.of(1968, JANUARY, 12 ))
      .gender(MALE)
      .build();
    var p3 = Person.builder()
      .firstName("p3")
      .dateOfBirth(LocalDate.of(1968, JANUARY, 12 ))
      .gender(MALE)
      .build();
    squad.addPlayer(new Player(p1));
    squad.addPlayer(new Player(p2));
    discipline.addOpponent(squad);
    discipline.addOpponent(new Player(p3));

    assertThat(discipline.getPlayers()).hasSize(3);
  }

  @Test
  void getCurrentRound_noRound() {
    var currentRound = new Discipline().getCurrentRound();
    assertThat(currentRound).isEmpty();
  }

  @Test
  void getCurrentRound_emptyRound() {
    discipline.addRound(new Round());
    var currentRound = discipline.getCurrentRound();
    assertThat(currentRound).isEmpty();
  }

  @Test
  void getCurrentRound_emptyGrooup() {
    var round = new Round();
    var group = new Group();
    round.appendGroup(group);
    discipline.addRound(round);
    var currentRound = discipline.getCurrentRound();

    assertThat(currentRound).isEmpty();
  }

  @Test
  void getCurrentRound() {
    var round = new Round();
    var group = new Group();
    group.addOpponent(new Player());
    round.appendGroup(group);
    discipline.addRound(round);
    var currentRound = discipline.getCurrentRound();

    assertThat(currentRound).isPresent();
  }
}