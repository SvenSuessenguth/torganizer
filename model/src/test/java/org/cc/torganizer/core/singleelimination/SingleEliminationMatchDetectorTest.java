package org.cc.torganizer.core.singleelimination;

import org.cc.torganizer.core.entities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SingleEliminationMatchDetectorTest {

	private SingleEliminationMatchDetector semd;

	@BeforeEach
	void before() {
		semd = new SingleEliminationMatchDetector();
	}

	@AfterEach
	void after() {
		semd = null;
	}

	@Test
	void testGetPendingMatchesNotOnLevelNull() {
		/*
		 * a \ |- 0 \ b / | |- 2 c \ | |- 1 / c /
		 */
		Group group = new Group();
		group.addOpponent(new Player(new Person("a", "a")));
		group.addOpponent(new Player(new Person("b", "b")));
		group.addOpponent(new Player(new Person("c", "c")));
		group.addOpponent(new Player(new Person("d", "d")));

		Match m0 = new Match(group.getOpponent(0), group.getOpponent(1));
		m0.addResult(new Result(0, 1, 0));
		m0.setFinishedTime(LocalDateTime.now());
		m0.setPosition(1);
		group.getMatches().add(m0);

		Match m1 = new Match(group.getOpponent(2), group.getOpponent(3));
		m1.addResult(new Result(0, 1, 0));
		m1.setFinishedTime(LocalDateTime.now());
		m1.setPosition(2);
		group.getMatches().add(m1);

		assertThat(semd.getPendingMatches(group)).hasSize(1);
	}

	@Test
	void testDetectMatchesUpperBracketMinimum() {
		Group group = new Group();
		group.addOpponent(new Player());
		group.addOpponent(new Player());
		group.addOpponent(new Player());
		group.addOpponent(new Player());
		group.addOpponent(new Player());
		group.addOpponent(new Player());
		group.addOpponent(new Player());
		group.addOpponent(new Player());

		assertThat(group.getPositionalOpponents()).hasSize(8);

		// Wenn noch keine Matches gespielt wurden, werden alle Matches als pending
		// gefunden
		// Grundsaetzlich muessen alle Matches Opponents haben
		List<Match> matchesUpperBracket = semd.getPendingMatches(group);
		assertThat(matchesUpperBracket).hasSize(7);
		for (Match match : matchesUpperBracket) {
			assertThat(match.getOpponents()).hasSizeGreaterThan(0);
		}
	}

	@Test
	void testCountLevels8() {
		assertThat(semd.getUpperBracketsNumberOfLevels(8)).isEqualTo(3);
	}

	@Test
	void testCountLevels128() {
		assertThat(semd.getUpperBracketsNumberOfLevels(128)).isEqualTo(7);
	}

	@Test
	void testCountLevels0() {
		assertThat(semd.getUpperBracketsNumberOfLevels(0)).isEqualTo(0);
	}

	@Test
	void testCountLevels2() {
		assertThat(semd.getUpperBracketsNumberOfLevels(2)).isEqualTo(1);
	}

	@Test
	void testDetectMatchesUpperBracket() {
		/*
		 * Spiel 0 kann nicht ermittelt werden Spiel 1 ergibt sich direkt aus den
		 * Spielern 0 und 1 Spiel 2 wurde bereits gespielt
		 * 
		 * 0 1 1 0 2 2 3
		 */
		Group group = new Group();
		group.addOpponent(new Player("0", "0"));
		group.addOpponent(new Player("1", "1"));
		group.addOpponent(new Player("2", "2"));
		group.addOpponent(new Player("3", "3"));

		// Ein Spiel mit einem Winner c
		Match m2 = new Match(group.getOpponent(2), group.getOpponent(3));
		m2.addResult(new Result(0, 1, 0));
		m2.setFinishedTime(LocalDateTime.now());
		m2.setRunning(false);
		m2.setPosition(2);
		group.getMatches().add(m2);

		List<Match> pendingMatches = semd.getPendingMatches(group);
		assertThat(pendingMatches).hasSize(2);
	}

	@Test
	void testGetMatchesOnLevel() {
		// n = 64 Opponents -> 32 Matches auf Level 0 ...
		// m = Anzahl der Matches gesamt = 32 + 16 + 8 + 4 + 2 + 1 = n -1
		int n = 64;
		int m = n - 1;
		Group group = new Group();
		// Matches
		for (int i = 0; i < m; i += 1) {
			Match match = new Match();
			match.setPosition(i);
			group.getMatches().add(match);
		}
		// Opponents
		for (int i = 0; i < n; i += 1) {
			Opponent opponent = new Player("" + i, "" + i);
			group.addOpponent(opponent);
		}

		// Level 0 -> Matches mit Index 31 bis 62
		List<Match> level0 = semd.getUpperBracketMatchesOnLevel(0, group);
		assertThat(level0).hasSize(32);
		for (int index = 31; index <= 62; index += 1) {
			assertThat(listContainsMatchWithIndex(level0, index)).isTrue();
		}

		// Level 1 -> Matches mit Index 15 bis 30
		List<Match> level1 = semd.getUpperBracketMatchesOnLevel(1, group);
		assertThat(level1).hasSize(16);
		for (int index = 15; index <= 30; index += 1) {
			assertThat(listContainsMatchWithIndex(level1, index)).isTrue();
		}

		// Level 2 -> Matches mit Index 7 bis 14
		List<Match> level2 = semd.getUpperBracketMatchesOnLevel(2, group);
		assertThat(level2).hasSize(8);
		for (int index = 7; index <= 14; index += 1) {
			assertThat(listContainsMatchWithIndex(level2, index)).isTrue();
		}
	}

	/**
	 * Hilfsmethode, um aus einer Liste von Matches herauszufinden, ob ein Match mit
	 * einem bestimmten Index enthalten ist.
	 */
	private boolean listContainsMatchWithIndex(List<Match> matches, int index) {
		for (Match match : matches) {
			if (match.getPosition() == index) {
				return true;
			}
		}
		return false;
	}

	@Test
	void testGetLosersOnLevel() {

		/*
		 * In Klammer steht (Spielindex, Gewinner) Unten ist ein Spiel noch nicht
		 * ausgetragen
		 * 
		 * 0 
		 *     0 vs. 1 (Match_3: 1:0) 
		 * 1 
		 *  	                          0 vs. 2 (Match_1: )  
		 * 2 
		 *  	 2 vs. 3 (Match_4: 1:0)
		 * 3 
		 *                                                   ? vs. ? (Match_0: )
		 * 4
		 * 	   4 vs. 5 (Match_5: 0:1)
		 * 5
		 *                              5 vs. ? (Match_2: )
		 * 6
		 *     6 vs. 7 (Match_6: -)
		 * 7
		 * 
		 */

		Group group = new Group();

		// Opponents
		for (long i = 0L; i < 7; i += 1) {
			Player opponent = new Player("" + i, "" + i);
			opponent.setId(i);
			group.addOpponent(opponent);
		}

		// Matches auf Level-0
		Match match3 = new Match(group.getOpponent(0), group.getOpponent(1));
		match3.addResult(new Result(0, 1, 0));
		match3.setPosition(3);
		match3.setFinishedTime(LocalDateTime.now());
		group.getMatches().add(match3);
		
		Match match4 = new Match(group.getOpponent(2), group.getOpponent(3));
		match4.addResult(new Result(0, 1, 0));
		match4.setPosition(4);
		match4.setFinishedTime(LocalDateTime.now());
		group.getMatches().add(match4);

		Match match5 = new Match(group.getOpponent(4), group.getOpponent(5));
		match5.addResult(new Result(0, 0, 1));
		match5.setPosition(5);
		match5.setFinishedTime(LocalDateTime.now());
		group.getMatches().add(match5);

		// Matches auf Level-1
		Match match1 = new Match(group.getOpponent(0), group.getOpponent(2));
		match1.addResult(new Result(0, 0, 1));
		match1.setPosition(1);
		group.getMatches().add(match1);

		// Verlierer auf Level 0 sind: 1, 3, 4, null
		List<Opponent> losersOnLevel0 = semd.getUpperBracketLosersOnLevel(0, group);

		assertThat(losersOnLevel0).hasSize(4);
		assertThat(((Player) losersOnLevel0.get(0)).getPerson().getFirstName()).isEqualTo("1");
		assertThat(((Player) losersOnLevel0.get(1)).getPerson().getFirstName()).isEqualTo("3");
		assertThat(((Player) losersOnLevel0.get(2)).getPerson().getFirstName()).isEqualTo("4");
		assertThat(losersOnLevel0.get(3)).isNull();

		// Verlierer auf Level 1 sind: 0, null
		List<Opponent> losersOnLevel1 = semd.getUpperBracketLosersOnLevel(1, group);
		assertThat(losersOnLevel1).hasSize(2);
		assertThat(((Player) losersOnLevel1.get(0)).getPerson().getFirstName()).isEqualTo("0");
		assertThat(losersOnLevel1.get(1)).isNull();

		// Verlierer auf Level 2 sind: null
		List<Opponent> losersOnLevel2 = semd.getUpperBracketLosersOnLevel(2, group);
		assertThat(losersOnLevel2).hasSize(1);
		assertThat(losersOnLevel2.get(0)).isNull();
	}

	@Test
	void testGetStartIndex() {
		// Gruppe mit 16 Opponents
		assertThat(semd.getUpperBracketStartIndex(0, 16)).isEqualTo(7);
		assertThat(semd.getUpperBracketStartIndex(1, 16)).isEqualTo(3);
		assertThat(semd.getUpperBracketStartIndex(2, 16)).isEqualTo(1);
		assertThat(semd.getUpperBracketStartIndex(3, 16)).isEqualTo(0);
	}

	@Test
	void testGetEndIndex() {
		// Gruppe mit 16 Opponents
		assertThat(semd.getUpperBracketEndIndex(0, 16)).isEqualTo(14);
		assertThat(semd.getUpperBracketEndIndex(1, 16)).isEqualTo(6);
		assertThat(semd.getUpperBracketEndIndex(2, 16)).isEqualTo(2);
		assertThat(semd.getUpperBracketEndIndex(3, 16)).isEqualTo(0);
	}
}
