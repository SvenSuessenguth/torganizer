package org.cc.torganizer.core.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Result;
import org.cc.torganizer.core.entities.Unknown;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author svens
 */
public class DoubleEliminationMatchDetectorTest {

	private DoubleEliminationMatchDetector demd;

	@Before
	public void before() {
		demd = new DoubleEliminationMatchDetector(null);
	}

	@After
	public void after() {
		demd = null;
	}

	@Test
	public void testCreatePendingMatchWithNull() {
		demd.setGroup(new Group());
		Match match = demd.createPendingMatch(0, 0, null, new Player());
		assertTrue(match == null);
	}

	@Test
	public void testCreatePendingMatch() {
		Group group = new Group();
		for (int i = 0; i < 4; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);
		Match match = demd.createPendingMatch(0, 0, new Player("a", "a"), new Player("b", "b"));
		assertNotNull(match);
	}

	@Test
	public void testAddMatchToListNull() {
		List<Match> matches = new ArrayList<Match>();
		demd.addMatchToList(matches, null);
		assertTrue(matches.isEmpty());
	}

	@Test
	public void testAddMatchToList() {
		List<Match> matches = new ArrayList<Match>();
		demd.addMatchToList(matches, new Match());
		assertFalse(matches.isEmpty());
	}

	@Test
	public void testIsFirstLevel() {
		assertTrue(demd.isFirstLevel(0));
		assertFalse(demd.isFirstLevel(1));
	}

	@Test
	public void testHasToMixUpperLowerBracket() {
		assertFalse(demd.hasToMixUpperLowerBracket(0));
		assertTrue(demd.hasToMixUpperLowerBracket(1));
		assertFalse(demd.hasToMixUpperLowerBracket(2));
		assertTrue(demd.hasToMixUpperLowerBracket(3));
	}

	@Test
	public void testCountMatchesUpToLevel_32_0() {

		// 32 Opponents -> 16 Loser -> 8 Matches
		Group group = new Group();
		for (int i = 0; i < 32; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);
		assertEquals(8, demd.countMatchesUpToLevel(0));
	}

	@Test
	public void testCountMatchesOnLevel() {
		Group group = new Group();
		for (int i = 0; i < 16; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);
		assertEquals(4, demd.countMatchesOnLevel(0));
		assertEquals(4, demd.countMatchesOnLevel(1));
		assertEquals(2, demd.countMatchesOnLevel(2));
		assertEquals(2, demd.countMatchesOnLevel(3));
	}

	@Test
	public void testCountMatchesOnLevelNull() {
		Group group = new Group();
		for (int i = 0; i < 16; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);
		assertEquals(0, demd.countMatchesOnLevel(-1));
	}

	@Test
	public void testCountMatchesUpToLevel_16_3() {

		Group group = new Group();
		for (int i = 0; i < 16; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);
		assertEquals(12, demd.countMatchesUpToLevel(3));
	}

	@Test
	public void testGetMatchIndex() {
		// Gruppe mit 16 Opponents zuweisen
		Group group = new Group();
		for (int i = 0; i < 16; i++) {
			group.addOpponent(new Player("" + i, "" + i));
		}
		demd.setGroup(group);

		// 16=Opponents, Level=0, LevelIndex=0 -> matchIndex = 15
		int matchIndex = demd.getMatchIndex(0, 0);
		assertEquals(15, matchIndex);

		// 16=Opponents, Level=3, LevelIndex=1 -> matchIndex = 26
		matchIndex = demd.getMatchIndex(3, 1);
		assertEquals(26, matchIndex);
	}

	@Test
	public void testGetMatchIndexNullNull() {
		Group group = new Group();
		for (int i = 0; i < 16; i++) {
			group.addOpponent(new Player("" + i, "" + i));
		}
		demd.setGroup(group);

		// 16 Opponents -> 15 Matches im Upper Level (index = 14)
		// Index im Lower Bracket beginnt mit 15

		assertEquals(15, demd.getMatchIndex(0, 0));
	}

	/**
	 * Alle Werte '-1' Output for printBracket(32) is: Level 1: [1, 2, 3, 4, 5, 6,
	 * 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
	 * 27, 28, 29, 30, 31, 32] Level 2: [9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4,
	 * 5, 6, 7, 8] Level 3: [3, 4, 1, 2, 7, 8, 5, 6] Level 4: [3, 4, 1, 2] Level 5:
	 * [1, 2]
	 */
	@Test
	public void testOrderUpperBracketLosers() {

		// 64 Opponents -> 32 auf Level 1 der Verlierer
		int players = 32;
		int level = 3;
		int playersInRound = new Double(players / Math.pow(2, level - 1)).intValue();
		int splitFactor = new Double(Math.pow(2.0, level - 1.0)).intValue();
		int reverseFactor = (level + 1) % 2;

		List<Opponent> losersOnLevel = new ArrayList<Opponent>();
		for (int i = 0; i < playersInRound; i++) {
			losersOnLevel.add(new Player("" + i, ""));
		}

		List<Opponent> orderedLosers = demd.orderUpperBracketLosers(losersOnLevel, splitFactor, reverseFactor);

		assertEquals("2", ((Player) orderedLosers.get(0)).getPerson().getFirstName());
		assertEquals("3", ((Player) orderedLosers.get(1)).getPerson().getFirstName());
		assertEquals("0", ((Player) orderedLosers.get(2)).getPerson().getFirstName());
		assertEquals("1", ((Player) orderedLosers.get(3)).getPerson().getFirstName());
		assertEquals("6", ((Player) orderedLosers.get(4)).getPerson().getFirstName());
		assertEquals("7", ((Player) orderedLosers.get(5)).getPerson().getFirstName());
		assertEquals("4", ((Player) orderedLosers.get(6)).getPerson().getFirstName());
		assertEquals("5", ((Player) orderedLosers.get(7)).getPerson().getFirstName());
	}

	@Test
	public void testGetPendingMatchesLowerBracketSimple() {
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
		m1.setIndex(1);
		group.getMatches().add(m1);

		Match m2 = new Match(players[2], players[3]);
		m2.addResult(new Result(0, 2, 3));
		m2.setFinishedTime(LocalDateTime.now());
		m2.setIndex(2);
		group.getMatches().add(m2);

		demd.setGroup(group);

		List<Match> matches = demd.getPendingMatchesLowerBracket();

		assertFalse(matches.isEmpty());
	}

	@Test
	public void testGetPendingMatchesLowerBracketComplex() {
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

		demd.setGroup(group);

		List<Match> matches = demd.getPendingMatchesLowerBracket();

		assertEquals(2, matches.size());
	}

	@Test
	public void testGetWinnersOnLevel() {
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

		demd.setGroup(group);

		List<Opponent> winnersOnLevel = demd.getWinnersOnLevel(0);
		assertEquals(2, winnersOnLevel.size());
	}

	@Test
	public void testGetPendingMatchesLowerBracketSemiComplex() {
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
		m3.setIndex(3);
		m3.setRunning(true);
		group.getMatches().add(m3);

		demd.setGroup(group);

		List<Match> matches = demd.getPendingMatchesLowerBracket();

		assertEquals(1, matches.size());
		assertTrue(matches.get(0).getGuest() instanceof Unknown);
	}

	@Test
	public void testGetPendingMatchesLowerBracketFinale() {
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
		demd.setGroup(group);

		Match finalMatch = demd.getPendingFinalMatch();

		assertNotNull(finalMatch);
	}

	@Test
	public void testGetPendingMatchesLowerBracketFinaleFinished() {
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
		demd.setGroup(group);

		Match finalMatch = demd.getPendingFinalMatch();

		assertTrue(finalMatch == null);
	}

	@Test
	public void testGetStartMatchIndex() {
		Group group = new Group();
		for (int i = 0; i < 8; i++) {
			Player player = new Player("p" + i, "p" + i);
			group.addOpponent(player);
		}
		demd.setGroup(group);

		assertEquals(7, demd.getStartMatchIndex(0));
	}

	@Test
	public void testGetEndMatchindex() {
		Group group = new Group();
		for (int i = 0; i < 8; i++) {
			Player player = new Player("p" + i, "p" + i);
			group.addOpponent(player);
		}
		demd.setGroup(group);

		assertEquals(8, demd.getEndMatchIndex(0));
	}

	@Test
	public void testGetStartMatchIndexEqualsEndIndex() {
		Group group = new Group();
		for (int i = 0; i < 4; i++) {
			Player player = new Player("p" + i, "p" + i);
			group.addOpponent(player);
		}
		demd.setGroup(group);

		assertEquals(3, demd.getEndMatchIndex(0));
	}

	@Test
	public void testGetEndMatchIndexEqualsStartIndex() {
		Group group = new Group();
		for (int i = 0; i < 4; i++) {
			Player player = new Player("p" + i, "p" + i);
			group.addOpponent(player);
		}
		demd.setGroup(group);

		assertEquals(3, demd.getStartMatchIndex(0));
	}

	@Test
	public void testGetFirstLevelMatches() {
		Group group = new Group();
		Opponent[] opponents = new Opponent[8];
		for (int i = 0; i < 8; i++) {
			Player player = new Player("p" + i, "p" + i);
			opponents[i] = player;
			group.addOpponent(player);
		}
		demd.setGroup(group);

		addMatch(group, 3, opponents[0], opponents[1], new Result(0, 1, 0));
		addMatch(group, 4, opponents[2], opponents[3], new Result(0, 0, 1));
		addMatch(group, 5, opponents[4], opponents[5], new Result(0, 2, 1));
		addMatch(group, 6, opponents[6], opponents[7], new Result(0, 2, 0));

		List<Match> firstLevelMatches = demd.getFirstLevelMatches();
		assertEquals(2, firstLevelMatches.size());
	}

	private Match addMatch(Group group, int matchIndex, Opponent home, Opponent guest, Result result) {
		Match match = new Match(home, guest);

		if (result != null) {
			match.addResult(result);
		}
		match.setFinishedTime(LocalDateTime.now());
		match.setIndex(matchIndex);
		group.getMatches().add(match);

		return match;
	}
}