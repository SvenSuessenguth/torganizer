package org.cc.torganizer.core.system;

import org.cc.torganizer.core.entities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author svens
 */
public class DoubleEliminationMatchDetectorTest {

	private DoubleEliminationMatchDetector demd;

	@BeforeEach
	public void before() {
		demd = new DoubleEliminationMatchDetector(null);
	}

	@AfterEach
	public void after() {
		demd = null;
	}

	@Test
	public void testCreatePendingMatchWithNull() {
		demd.setGroup(new Group());
		Match match = demd.createPendingMatch(0, 0, null, new Player());
		assertThat(match, is(nullValue()));
	}

	@Test
	public void testCreatePendingMatch() {
		Group group = new Group();
		for (int i = 0; i < 4; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);
		Match match = demd.createPendingMatch(0, 0, new Player("a", "a"), new Player("b", "b"));
		assertThat(match,is(not(nullValue())));
	}

	@Test
	public void testAddMatchToListNull() {
		List<Match> matches = new ArrayList<>();
		demd.addMatchToList(matches, null);
		assertThat(matches, is(empty()));
	}

	@Test
	public void testAddMatchToList() {
		List<Match> matches = new ArrayList<>();
		demd.addMatchToList(matches, new Match());
		assertThat(matches, is(not(empty())));
	}

	@Test
	public void testIsFirstLevel() {
		assertThat(demd.isFirstLevel(0), is(true));
		assertThat(demd.isFirstLevel(1), is(false));
	}

	@Test
	public void testHasToMixUpperLowerBracket() {
		assertThat(demd.hasToMixUpperLowerBracket(0), is(false));
		assertThat(demd.hasToMixUpperLowerBracket(1), is(true));
		assertThat(demd.hasToMixUpperLowerBracket(2), is(false));
		assertThat(demd.hasToMixUpperLowerBracket(3), is(true));
	}

	@Test
	public void testCountMatchesUpToLevel_32_0() {

		// 32 Opponents -> 16 Loser -> 8 Matches
		Group group = new Group();
		for (int i = 0; i < 32; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);
		assertThat(demd.countMatchesUpToLevel(0), is(8));
	}

	@Test
	public void testCountMatchesOnLevel() {
		Group group = new Group();
		for (int i = 0; i < 16; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);

		assertThat(demd.countMatchesOnLevel(0), is(4));
		assertThat(demd.countMatchesOnLevel(1), is(4));
		assertThat(demd.countMatchesOnLevel(2), is(2));
		assertThat(demd.countMatchesOnLevel(3), is(2));
	}

	@Test
	public void testCountMatchesOnLevelNull() {
		Group group = new Group();
		for (int i = 0; i < 16; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);

		assertThat(demd.countMatchesOnLevel(-1), is(0));
	}

	@Test
	public void testCountMatchesUpToLevel_16_3() {

		Group group = new Group();
		for (int i = 0; i < 16; i++) {
			group.addOpponent(new Player());
		}
		demd.setGroup(group);
		assertThat(demd.countMatchesUpToLevel(3), is(12));
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
		assertThat(matchIndex, is(15));

		// 16=Opponents, Level=3, LevelIndex=1 -> matchIndex = 26
		matchIndex = demd.getMatchIndex(3, 1);
		assertThat(matchIndex,is(26));
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

		assertThat(demd.getMatchIndex(0, 0), is(15));
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

		List<Opponent> losersOnLevel = new ArrayList<>();
		for (int i = 0; i < playersInRound; i++) {
			losersOnLevel.add(new Player("" + i, ""));
		}

		List<Opponent> orderedLosers = demd.orderUpperBracketLosers(losersOnLevel, splitFactor, reverseFactor);

		assertThat(((Player) orderedLosers.get(0)).getPerson().getFirstName(), is("2"));
		assertThat(((Player) orderedLosers.get(1)).getPerson().getFirstName(), is("3"));
		assertThat(((Player) orderedLosers.get(2)).getPerson().getFirstName(), is("0"));
		assertThat(((Player) orderedLosers.get(3)).getPerson().getFirstName(), is("1"));
		assertThat(((Player) orderedLosers.get(4)).getPerson().getFirstName(), is("6"));
		assertThat(((Player) orderedLosers.get(5)).getPerson().getFirstName(), is("7"));
		assertThat(((Player) orderedLosers.get(6)).getPerson().getFirstName(), is("4"));
		assertThat(((Player) orderedLosers.get(7)).getPerson().getFirstName(), is("5"));
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
		m1.setPosition(1);
		group.getMatches().add(m1);

		Match m2 = new Match(players[2], players[3]);
		m2.addResult(new Result(0, 2, 3));
		m2.setFinishedTime(LocalDateTime.now());
		m2.setPosition(2);
		group.getMatches().add(m2);

		demd.setGroup(group);

		List<Match> matches = demd.getPendingMatchesLowerBracket();

		assertThat(matches, is(not(empty())));
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

		assertThat(matches, hasSize(2));
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
		assertThat(winnersOnLevel, hasSize(2));
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
		m3.setPosition(3);
		m3.setRunning(true);
		group.getMatches().add(m3);

		demd.setGroup(group);

		List<Match> matches = demd.getPendingMatchesLowerBracket();

		assertThat(matches, hasSize(1));
		assertThat(matches.get(0).getGuest(), is(instanceOf(Unknown.class)));
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

		assertThat(finalMatch, is(not(nullValue())));
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

		assertThat(finalMatch, is(nullValue()));
	}

	@Test
	public void testGetStartMatchIndex() {
		Group group = new Group();
		for (int i = 0; i < 8; i++) {
			Player player = new Player("p" + i, "p" + i);
			group.addOpponent(player);
		}
		demd.setGroup(group);

		assertThat(demd.getStartMatchIndex(0), is(7));
	}

	@Test
	public void testGetEndMatchindex() {
		Group group = new Group();
		for (int i = 0; i < 8; i++) {
			Player player = new Player("p" + i, "p" + i);
			group.addOpponent(player);
		}
		demd.setGroup(group);

		assertThat(demd.getEndMatchIndex(0), is(8));
	}

	@Test
	public void testGetStartMatchIndexEqualsEndIndex() {
		Group group = new Group();
		for (int i = 0; i < 4; i++) {
			Player player = new Player("p" + i, "p" + i);
			group.addOpponent(player);
		}
		demd.setGroup(group);

		assertThat(demd.getEndMatchIndex(0), is(3));
	}

	@Test
	public void testGetEndMatchIndexEqualsStartIndex() {
		Group group = new Group();
		for (int i = 0; i < 4; i++) {
			Player player = new Player("p" + i, "p" + i);
			group.addOpponent(player);
		}
		demd.setGroup(group);

		assertThat(demd.getStartMatchIndex(0), is(3));
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
		assertThat(firstLevelMatches, hasSize(2));
	}

	private Match addMatch(Group group, int matchIndex, Opponent home, Opponent guest, Result result) {
		Match match = new Match(home, guest);

		if (result != null) {
			match.addResult(result);
		}
		match.setFinishedTime(LocalDateTime.now());
		match.setPosition(matchIndex);
		group.getMatches().add(match);

		return match;
	}
}