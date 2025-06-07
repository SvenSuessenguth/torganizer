package org.cc.torganizer.core.roundrobin;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Result;
import org.cc.torganizer.core.entities.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoundRobinMatchDetectorTest {

  private RoundRobinMatchDetector rrmd;
  private List<Opponent> players;
  private List<Opponent> teams;

  private Group group;

  @BeforeEach
  void before() {
    group = new Group();
    rrmd = new RoundRobinMatchDetector();

    // 5 player
    players = new ArrayList<>();
    players.add(new Player("p0", "p0"));
    players.add(new Player("p1", "p1"));
    players.add(new Player("p2", "p2"));
    players.add(new Player("p3", "p3"));
    players.add(new Player("p4", "p4"));

    // 5 Teams
    teams = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Team team = new Team();
      for (int j = 0; j < 3; j++) {
        team.getPlayers().add(new Player("p" + i + "." + j, ""));
      }
      teams.add(team);
    }
  }

  @AfterEach
  void after() {
    rrmd = null;
    players = null;
    teams = null;
    group = null;
  }

  @Test
  void testMatchIndex() {
    group.addOpponent(new Player("a", "a"));
    group.addOpponent(new Player("b", "b"));
    group.addOpponent(new Player("c", "c"));

    /* a:b -> 0
     * a:c -> 1
     * b:c -> 2
     */

    List<Match> matches = rrmd.getPendingMatches(group);

    assertThat(matches.getFirst().getPosition().intValue()).isEqualTo(1);
    assertThat(matches.get(1).getPosition().intValue()).isEqualTo(2);
    assertThat(matches.get(2).getPosition().intValue()).isEqualTo(5);
  }

  @Test
  void testGetPendingMatches() {
    for (Opponent opponent : players) {
      group.addOpponent(opponent);
    }

    Match m0 = rrmd.getPendingMatches(group).getFirst();
    m0.addResult(new Result(0, 1, 0));
    m0.setFinishedTime(LocalDateTime.now());
    m0.setRunning(false);
    group.getMatches().add(m0);

    assertThat(rrmd.getPendingMatches(group)).hasSize(9);
  }

  @Test
  void testGetPendingMatchesFromScratch() {
    // bisher sind noch keine Matches gespielt worden    
    for (Opponent opponent : players) {
      group.addOpponent(opponent);
    }
    assertThat(rrmd.getPendingMatches(group)).hasSize(10);
  }
}
