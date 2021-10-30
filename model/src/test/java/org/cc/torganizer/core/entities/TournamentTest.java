package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TournamentTest {

  private Tournament tournament;

  @BeforeEach
  public void beforeEach() {
    tournament = new Tournament();
  }

  @Test
  void testGetRunningMatches_noDiscipline() {
    List<Match> runningMatches = tournament.getRunningMatches();
    assertThat(runningMatches).isEmpty();
  }

  @Test
  void testGetRunningMatches_oneDisciplineWithNoRounds() {
    tournament.addDiscipline(new Discipline());
    List<Match> runningMatches = tournament.getRunningMatches();

    assertThat(runningMatches).isEmpty();
  }

  @Test
  void testGetRunningMatches_oneDisciplineWithRounds_noGroups() {
    Discipline discipline = new Discipline();
    discipline.addRound(new Round());

    tournament.addDiscipline(discipline);
    List<Match> runningMatches = tournament.getRunningMatches();

    assertThat(runningMatches).isEmpty();
  }

  @Test
  void testGetRunningMatches_oneDisciplineWithRoundsAndGroups_noRunningMatches() {
    Discipline discipline = new Discipline();
    Round round = new Round();
    round.appendGroup(new Group());
    discipline.addRound(round);

    tournament.addDiscipline(discipline);
    List<Match> runningMatches = tournament.getRunningMatches();

    assertThat(runningMatches).isEmpty();
  }

  @Test
  void testGetRunningMatches_oneDisciplineWithRoundsAndGroups_withRunningMatches() {
    Discipline discipline = new Discipline();
    Round round = new Round();
    Group group = new Group();
    group.addOpponent(new Player());
    Match match = new Match(new Player(), new Player());
    match.setRunning(true);
    group.getMatches().add(match);
    round.appendGroup(group);
    discipline.addRound(round);

    tournament.addDiscipline(discipline);
    List<Match> runningMatches = tournament.getRunningMatches();

    assertThat(runningMatches).isNotEmpty();
  }

  @Test
  void testGetFinishedMatches() {
    Discipline discipline = new Discipline();
    Round round = new Round();
    Group group = new Group();
    group.addOpponent(new Player());
    Match match = new Match(new Player(), new Player());
    match.setRunning(false);
    match.setFinishedTime(LocalDateTime.now());
    group.getMatches().add(match);
    round.appendGroup(group);
    discipline.addRound(round);

    tournament.addDiscipline(discipline);
    List<Match> finishedMatches = tournament.getFinishedMatches();

    assertThat(finishedMatches).isNotEmpty().hasSize(1);
  }
}