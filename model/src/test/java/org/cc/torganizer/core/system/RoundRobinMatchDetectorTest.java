package org.cc.torganizer.core.system;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Result;
import org.cc.torganizer.core.entities.Team;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RoundRobinMatchDetectorTest {

  private RoundRobinMatchDetector rrmd;
  private List<Opponent> players;
  private List<Opponent> teams;

  @Before
  public void before() {
    Group group = new Group();
    rrmd = new RoundRobinMatchDetector(group);

    // 5 player
    players = new ArrayList<Opponent>();
    players.add(new Player("p0", "p0"));
    players.add(new Player("p1", "p1"));
    players.add(new Player("p2", "p2"));
    players.add(new Player("p3", "p3"));
    players.add(new Player("p4", "p4"));

    // 5 Teams
    teams = new ArrayList<Opponent>();
    for (int i = 0; i < 5; i++) {
      Team team = new Team();
      for (int j = 0; j < 3; j++) {
        team.getPlayers().add(new Player("p" + i + "." + j, ""));
      }
      teams.add(team);
    }
  }

  @After
  public void after() {
    rrmd = null;
    players = null;
    teams = null;
  }

  @Test
  public void testMatchIndex() {
    Group group = rrmd.getGroup();
    group.addOpponent(new Player("a", "a"));
    group.addOpponent(new Player("b", "b"));
    group.addOpponent(new Player("c", "c"));

    /* a:b -> 0
     * a:c -> 1
     * b:c -> 2
     */

    List<Match> matches = rrmd.getPendingMatches();

    assertEquals(1, matches.get(0).getPosition().intValue());
    assertEquals(2, matches.get(1).getPosition().intValue());
    assertEquals(5, matches.get(2).getPosition().intValue());
  }

  @Test
  public void testGetPendingMatches() {
    Group group = rrmd.getGroup();
    for (Opponent opponent : players) {
      group.addOpponent(opponent);
    }

    Match m0 = rrmd.getPendingMatches().get(0);
    m0.addResult(new Result(0, 1, 0));
    m0.setFinishedTime(LocalDateTime.now());
    m0.setRunning(false);
    group.getMatches().add(m0);

    assertEquals(9, rrmd.getPendingMatches().size());
  }

  @Test
  public void testGetPendingMatchesFromScratch() {
    // bisher sind noch keine Matches gespielt worden    
    // Group mit 5 Player -> 4+3+2+1 = 10 Spiele 
    Group group = rrmd.getGroup();
    for (Opponent opponent : players) {
      group.addOpponent(opponent);
    }
    assertEquals(10, rrmd.getPendingMatches().size());
  }
}
