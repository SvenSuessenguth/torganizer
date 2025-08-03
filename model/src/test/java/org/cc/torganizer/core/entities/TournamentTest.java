package org.cc.torganizer.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TournamentTest {

  private Tournament tournament;

  @BeforeEach
  void beforeEach() {
    tournament = new Tournament();
  }

  @Test
  void testGetRunningMatches_noDiscipline() {
    var runningMatches = tournament.getRunningMatches();
    assertThat(runningMatches).isEmpty();
  }

  @Test
  void testGetRunningMatches_oneDisciplineWithNoRounds() {
    tournament.addDiscipline(new Discipline());
    var runningMatches = tournament.getRunningMatches();

    assertThat(runningMatches).isEmpty();
  }

  @Test
  void testGetRunningMatches_oneDisciplineWithRounds_noGroups() {
    var discipline = new Discipline();
    discipline.addRound(new Round());

    tournament.addDiscipline(discipline);
    var runningMatches = tournament.getRunningMatches();

    assertThat(runningMatches).isEmpty();
  }

  @Test
  void testGetRunningMatches_oneDisciplineWithRoundsAndGroups_noRunningMatches() {
    var discipline = new Discipline();
    var round = new Round();
    round.appendGroup(new Group());
    discipline.addRound(round);

    tournament.addDiscipline(discipline);
    var runningMatches = tournament.getRunningMatches();

    assertThat(runningMatches).isEmpty();
  }

  @Test
  void testGetRunningMatches_oneDisciplineWithRoundsAndGroups_withRunningMatches() {
    var discipline = new Discipline();
    var round = new Round();
    var group = new Group();
    group.addOpponent(new Player());
    var match = new Match(new Player(), new Player());
    match.setRunning(true);
    group.getMatches().add(match);
    round.appendGroup(group);
    discipline.addRound(round);

    tournament.addDiscipline(discipline);
    var runningMatches = tournament.getRunningMatches();

    assertThat(runningMatches).isNotEmpty();
  }

  @Test
  void testGetFinishedMatches() {
    var discipline = new Discipline();
    var round = new Round();
    var group = new Group();
    group.addOpponent(new Player());
    var match = new Match(new Player(), new Player());
    match.setRunning(false);
    match.setFinishedTime(LocalDateTime.now());
    group.getMatches().add(match);
    round.appendGroup(group);
    discipline.addRound(round);

    tournament.addDiscipline(discipline);
    var finishedMatches = tournament.getFinishedMatches();

    assertThat(finishedMatches).isNotEmpty().hasSize(1);
  }
}