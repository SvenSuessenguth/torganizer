package org.cc.torganizer.core.doubleelimination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Result;
import org.cc.torganizer.core.entities.Unknown;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author svens
 */
class DoubleEliminationMatchDetectorTestDoubleEliminationMatchDetectorTest {

  private DoubleEliminationMatchDetector demd;

  @BeforeEach
  void before() {
    demd = new DoubleEliminationMatchDetector();
  }

  @AfterEach
  void after() {
    demd = null;
  }

  @Test
  void testCreatePendingMatchWithNull() {

    assertThrows(NullPointerException.class, () -> demd.createPendingMatch(0, 0, null, new Player(), null));
  }

  @Test
  void testCreatePendingMatch() {
    Group group = new Group();
    for (int i = 0; i < 4; i++) {
      group.addOpponent(new Player());
    }

    Match match = demd.createPendingMatch(0, 0, new Player("a", "a"), new Player("b", "b"), group);

    assertThat(match).isNotNull();
  }

  @Test
  void testAddMatchToListNull() {
    List<Match> matches = new ArrayList<>();
    demd.addMatchToList(matches, null);

    assertThat(matches).isEmpty();
  }

  @Test
  void testAddMatchToList() {
    List<Match> matches = new ArrayList<>();
    demd.addMatchToList(matches, new Match());

    assertThat(matches).isNotEmpty();
  }

  @Test
  void testIsFirstLevel() {
    assertThat(demd.isFirstLevel(0)).isTrue();
    assertThat(demd.isFirstLevel(1)).isFalse();
  }

  @Test
  void testHasToMixUpperLowerBracket() {
    assertThat(demd.hasToMixUpperLowerBracket(0)).isFalse();
    assertThat(demd.hasToMixUpperLowerBracket(1)).isTrue();
    assertThat(demd.hasToMixUpperLowerBracket(2)).isFalse();
    assertThat(demd.hasToMixUpperLowerBracket(3)).isTrue();
  }

  @Test
  void testCountMatchesUpToLevel_32_0() {

    // 32 Opponents -> 16 Loser -> 8 Matches
    assertThat(demd.countMatchesUpToLevel(0, 32)).isEqualTo(8);
  }

  @Test
  void testCountMatchesOnLevel() {
    assertThat(demd.countMatchesOnLevel(0, 16)).isEqualTo(4);
    assertThat(demd.countMatchesOnLevel(1, 16)).isEqualTo(4);
    assertThat(demd.countMatchesOnLevel(2, 16)).isEqualTo(2);
    assertThat(demd.countMatchesOnLevel(3, 16)).isEqualTo(2);
  }

  @Test
  void testCountMatchesOnLevelNull() {
    assertThat(demd.countMatchesOnLevel(-1, 16)).isZero();
  }

  @Test
  void testCountMatchesUpToLevel_16_3() {
    assertThat(demd.countMatchesUpToLevel(3, 16)).isEqualTo(12);
  }

  @Test
  void testGetMatchIndex() {
    // Gruppe mit 16 Opponents
    // 16=Opponents, Level=0, LevelIndex=0 -> matchIndex = 15
    int matchIndex = demd.getMatchIndex(0, 0, 16);
    assertThat(matchIndex).isEqualTo(15);

    // 16=Opponents, Level=3, LevelIndex=1 -> matchIndex = 26
    matchIndex = demd.getMatchIndex(3, 1, 16);
    assertThat(matchIndex).isEqualTo(26);
  }

  @Test
  void testGetMatchIndexNullNull() {
    // 16 Opponents -> 15 Matches im Upper Level (index = 14)
    // Index im Lower Bracket beginnt mit 15
    assertThat(demd.getMatchIndex(0, 0, 16)).isEqualTo(15);
  }

  /**
   * Alle Werte '-1' Output for printBracket(32) is: Level 1: [1, 2, 3, 4, 5, 6,
   * 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
   * 27, 28, 29, 30, 31, 32] Level 2: [9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4,
   * 5, 6, 7, 8] Level 3: [3, 4, 1, 2, 7, 8, 5, 6] Level 4: [3, 4, 1, 2] Level 5:
   * [1, 2]
   */
  @Test
  void testOrderUpperBracketLosers() {

    // 64 Opponents -> 32 auf Level 1 der Verlierer
    int players = 32;
    int level = 3;
    int playersInRound = Double.valueOf(players / Math.pow(2, level - 1)).intValue();
    int splitFactor = Double.valueOf(Math.pow(2.0, level - 1.0)).intValue();
    int reverseFactor = (level + 1) % 2;

    List<Opponent> losersOnLevel = new ArrayList<>();
    for (int i = 0; i < playersInRound; i++) {
      losersOnLevel.add(new Player("" + i, ""));
    }

    List<Opponent> orderedLosers = demd.orderUpperBracketLosers(losersOnLevel, splitFactor, reverseFactor);

    assertThat(((Player) orderedLosers.get(0)).getPerson().getFirstName()).isEqualTo("2");
    assertThat(((Player) orderedLosers.get(1)).getPerson().getFirstName()).isEqualTo("3");
    assertThat(((Player) orderedLosers.get(2)).getPerson().getFirstName()).isEqualTo("0");
    assertThat(((Player) orderedLosers.get(3)).getPerson().getFirstName()).isEqualTo("1");
    assertThat(((Player) orderedLosers.get(4)).getPerson().getFirstName()).isEqualTo("6");
    assertThat(((Player) orderedLosers.get(5)).getPerson().getFirstName()).isEqualTo("7");
    assertThat(((Player) orderedLosers.get(6)).getPerson().getFirstName()).isEqualTo("4");
    assertThat(((Player) orderedLosers.get(7)).getPerson().getFirstName()).isEqualTo("5");
  }

  @Test
  void testGetPendingMatchesLowerBracketSimple() {
    /*
     * p0 \ |- 1:0 (m1) - p0 \ p1 / | |- egal (m0) - p3 p2 \ | |- 2:3 (m2) - p3 / p3
     * /
     *
     *
     * p1 \ | muss gefunden werden p2 /
     *
     */
    Group group = new Group();
    Player[] players = new Player[4];
    for (int i = 0; i < 4; i++) {
      Player player = new Player("p" + i, "p" + i);
      players[i] = player;
      group.addOpponent(player);
    }
    Match m1 = new Match(players[0], players[1]);
    m1.addResult(new Result(0, 1, 0));
    m1.setFinishedTime(LocalDateTime.now());
    m1.setPosition(1);
    group.getMatches().add(m1);

    Match m2 = new Match(players[2], players[3]);
    m2.addResult(new Result(0, 2, 3));
    m2.setFinishedTime(LocalDateTime.now());
    m2.setPosition(2);
    group.getMatches().add(m2);

    List<Match> matches = demd.getPendingMatchesLowerBracket(group);

    assertThat(matches).isNotEmpty();
  }

  @Test
  void testGetPendingMatchesLowerBracketComplex() {
    /*
     * p0 \ |- 1:0 (m3) - p0 \ p1 / | |- 3:4 (m1) - p3 \ p2 \ | | |- 2:3 (m4) - p3 /
     * | p3 / | |- egal (m0) p4 \ | |- 2:1 (m5) p4 \ | p5 / | | |- 2:1 (m2) - p4 /
     * p6 \ | |- 1:3 (m6) p7 / p7 /
     *
     *
     *
     *
     * p1 \ |- 1:0 (m7) p1 ab hier einmischen von p0 und p7 p2 / zwei Matches
     * muessen gefunden werden
     *
     * p5 \ |- 0:1 (m8) p6 p6 /
     *
     */
    Group group = new Group();
    Player[] players = new Player[8];
    for (int i = 0; i < 8; i++) {
      Player player = new Player("p" + i, "p" + i);
      players[i] = player;
      group.addOpponent(player);
    }
    addMatch(group, 3, players[0], players[1], new Result(0, 1, 0));
    addMatch(group, 4, players[2], players[3], new Result(0, 2, 3));
    addMatch(group, 5, players[4], players[5], new Result(0, 2, 1));
    addMatch(group, 6, players[6], players[7], new Result(0, 1, 3));
    addMatch(group, 1, players[0], players[3], new Result(0, 3, 4));
    addMatch(group, 2, players[4], players[7], new Result(0, 2, 1));
    addMatch(group, 7, players[1], players[2], new Result(0, 1, 0));
    addMatch(group, 8, players[5], players[6], new Result(0, 0, 1));

    List<Match> matches = demd.getPendingMatchesLowerBracket(group);

    assertThat(matches).hasSize(2);
  }

  @Test
  void testGetWinnersOnLevel() {
    Group group = new Group();
    Player[] players = new Player[8];
    for (int i = 0; i < 8; i++) {
      Player player = new Player("p" + i, "p" + i);
      players[i] = player;
      group.addOpponent(player);
    }
    // matches im upper bracket
    addMatch(group, 3, players[0], players[1], new Result(0, 1, 0));
    addMatch(group, 4, players[2], players[3], new Result(0, 2, 3));
    addMatch(group, 5, players[4], players[5], new Result(0, 2, 1));
    addMatch(group, 6, players[6], players[7], new Result(0, 1, 3));
    addMatch(group, 1, players[0], players[3], new Result(0, 3, 4));
    addMatch(group, 2, players[4], players[7], new Result(0, 2, 1));

    // matches im lower bracket
    addMatch(group, 7, players[1], players[2], new Result(0, 1, 0));
    addMatch(group, 8, players[5], players[6], new Result(0, 0, 1));

    List<Opponent> winnersOnLevel = demd.getWinnersOnLevel(0, group);

    assertThat(winnersOnLevel).hasSize(2);
  }

  @Test
  void testGetPendingMatchesLowerBracketSemiComplex() {
    /*
     * Es muss ein peding match mit Guest = unknown gefunden werden weil Match 3
     * noch nicht beendet wurde.
     *
     * p0 \ |- 1:0 (m1) - p0 \ p1 / | |- 1:0 (m0) p2 \ | |- 0:1 (m2) - p3 / p3 /
     *
     *
     * p1 \ |- running (m0+3) p2 /
     */
    Group group = new Group();
    Player[] players = new Player[4];
    for (int i = 0; i < 4; i++) {
      Player player = new Player("p" + i, "p" + i);
      players[i] = player;
      group.addOpponent(player);
    }
    addMatch(group, 1, players[0], players[1], new Result(0, 1, 0));
    addMatch(group, 2, players[2], players[3], new Result(0, 0, 1));
    addMatch(group, 0, players[0], players[3], new Result(0, 1, 0));
    Match m3 = new Match(players[1], players[3]);
    m3.setPosition(3);
    m3.setRunning(true);
    group.getMatches().add(m3);

    List<Match> matches = demd.getPendingMatchesLowerBracket(group);

    assertThat(matches).hasSize(1);
    assertThat(matches.get(0).getGuest()).isInstanceOf(Unknown.class);
  }

  @Test
  void testGetPendingMatchesLowerBracketFinale() {
    /*
     * Finale muss gefunden werden
     *
     * p0 \ |- 1:0 (m1) - p0 \ p1 / | |- 1:0 (m0) - p0 \ p2 \ | | |- 0:1 (m2) - p3 /
     * |- p3 / | p3 \ | |- 1:0 (m0+4) - p3 / p1 \ | |- 1:0 (m0+3) p1 / p2 /
     */
    Group group = new Group();
    Player[] players = new Player[4];
    for (int i = 0; i < 4; i++) {
      Player player = new Player("p" + i, "p" + i);
      players[i] = player;
      group.addOpponent(player);
    }
    addMatch(group, 1, players[0], players[1], new Result(0, 1, 0));
    addMatch(group, 2, players[2], players[3], new Result(0, 0, 1));
    addMatch(group, 0, players[0], players[3], new Result(0, 1, 0));
    addMatch(group, 3, players[1], players[2], new Result(0, 1, 0));
    addMatch(group, 4, players[3], players[1], new Result(0, 1, 0));

    Match finalMatch = demd.getPendingFinalMatch(group);

    assertThat(finalMatch).isNotNull();
  }

  @Test
  void testGetPendingMatchesLowerBracketFinaleFinished() {
    /*
     * Finale ist bereits gespielt
     *
     * p0 \ |- 1:0 (m1) - p0 \ p1 / | |- 1:0 (m0) - p0 \ p2 \ | | |- 0:1 (m2) - p3 /
     * |- 1:0 (m0+5) p3 / | p3 \ | |- 1:0 (m0+4) - p3 / p1 \ | |- 1:0 (m0+3) p1 / p2
     * /
     */
    Group group = new Group();
    Player[] players = new Player[4];
    for (int i = 0; i < 4; i++) {
      Player player = new Player("p" + i, "p" + i);
      players[i] = player;
      group.addOpponent(player);
    }
    addMatch(group, 1, players[0], players[1], new Result(0, 1, 0));
    addMatch(group, 2, players[2], players[3], new Result(0, 0, 1));
    addMatch(group, 0, players[0], players[3], new Result(0, 1, 0));
    addMatch(group, 3, players[1], players[2], new Result(0, 1, 0));
    addMatch(group, 4, players[3], players[1], new Result(0, 1, 0));
    addMatch(group, 5, players[0], players[3], new Result(0, 1, 0));

    Match finalMatch = demd.getPendingFinalMatch(group);

    assertThat(finalMatch).isNull();
  }

  @Test
  void testGetStartMatchIndex() {
    assertThat(demd.getStartMatchIndex(0, 8)).isEqualTo(7);
  }

  @Test
  void testGetEndMatchindex() {
    assertThat(demd.getEndMatchIndex(0, 8)).isEqualTo(8);
  }

  @Test
  void testGetStartMatchIndexEqualsEndIndex() {
    assertThat(demd.getEndMatchIndex(0, 4)).isEqualTo(3);
  }

  @Test
  void testGetEndMatchIndexEqualsStartIndex() {
    assertThat(demd.getStartMatchIndex(0, 4)).isEqualTo(3);
  }

  @Test
  void testGetFirstLevelMatches() {
    Group group = new Group();
    Opponent[] opponents = new Opponent[8];
    for (int i = 0; i < 8; i++) {
      Player player = new Player("p" + i, "p" + i);
      opponents[i] = player;
      group.addOpponent(player);
    }

    addMatch(group, 3, opponents[0], opponents[1], new Result(0, 1, 0));
    addMatch(group, 4, opponents[2], opponents[3], new Result(0, 0, 1));
    addMatch(group, 5, opponents[4], opponents[5], new Result(0, 2, 1));
    addMatch(group, 6, opponents[6], opponents[7], new Result(0, 2, 0));

    List<Match> firstLevelMatches = demd.getFirstLevelMatches(group);

    assertThat(firstLevelMatches).hasSize(2);
  }

  private void addMatch(Group group, int matchIndex, Opponent home, Opponent guest, Result result) {
    Match match = new Match(home, guest);

    if (result != null) {
      match.addResult(result);
    }
    match.setFinishedTime(LocalDateTime.now());
    match.setPosition(matchIndex);
    group.getMatches().add(match);
  }
}