package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

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
  void testGetRunningMatches_oneDisciplineWithRounds_noRunningMatches() {
    Discipline discipline = new Discipline();
    discipline.addRound(new Round());

    tournament.addDiscipline(discipline);
    List<Match> runningMatches = tournament.getRunningMatches();

    assertThat(runningMatches).isEmpty();
  }
}