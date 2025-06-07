package org.cc.torganizer.core.entities;

import static java.time.LocalDate.of;
import static java.time.Month.JANUARY;
import static java.time.Month.MAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.AGE_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.GENDER_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.cc.torganizer.core.exceptions.RestrictionException;
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
    assertThat(restrictions.getFirst()).isInstanceOf(AgeRestriction.class);
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

  @Test
  void testGetLastRound_noRoundAvailable() {
    Round actual = discipline.getLastRound();
    assertThat(actual).isNull();
  }

  @Test
  void testGetLastRound_roundsAvailable() {
    discipline.addRound(new Round());
    discipline.addRound(new Round());
    discipline.addRound(new Round());
    Round actual = discipline.getLastRound();
    assertThat(actual).isNotNull();
    assertThat(actual.getPosition()).isEqualTo(2);
  }

  @Test
  void addOpponent_restriction() {
    Player player = new Player("a", "a");
    player.getPerson().setGender(FEMALE);

    assertThatThrownBy(() -> discipline.addOpponent(player)).isInstanceOf(RestrictionException.class);
  }

  @Test
  void addOpponent() {
    Player player = new Player("a", "a");
    player.getPerson().setGender(MALE);

    int countBefore = discipline.getOpponents().size();
    discipline.addOpponent(player);
    int countAfter = discipline.getOpponents().size();

    assertThat(countBefore + 1).isEqualTo(countAfter);
  }

  @Test
  void getPlayers() {
    Discipline discipline = new Discipline();

    Squad squad = new Squad();
    squad.addPlayer(new Player(new Person()));
    squad.addPlayer(new Player(new Person()));
    discipline.addOpponent(squad);
    discipline.addOpponent(new Player());

    assertThat(discipline.getPlayers()).hasSize(3);
  }

  @Test
  void getCurrentRound_noRound() {
    Optional<Round> currentRound = new Discipline().getCurrentRound();
    assertThat(currentRound).isEmpty();
  }

  @Test
  void getCurrentRound_emptyRound() {
    Discipline discipline = new Discipline();
    discipline.addRound(new Round());
    Optional<Round> currentRound = discipline.getCurrentRound();
    assertThat(currentRound).isEmpty();
  }

  @Test
  void getCurrentRound_emptyGrooup() {
    Discipline discipline = new Discipline();
    Round round = new Round();
    Group group = new Group();
    round.appendGroup(group);
    discipline.addRound(round);
    Optional<Round> currentRound = discipline.getCurrentRound();

    assertThat(currentRound).isEmpty();
  }

  @Test
  void getCurrentRound() {
    Discipline discipline = new Discipline();
    Round round = new Round();
    Group group = new Group();
    group.addOpponent(new Player());
    round.appendGroup(group);
    discipline.addRound(round);
    Optional<Round> currentRound = discipline.getCurrentRound();

    assertThat(currentRound).isPresent();
  }
}