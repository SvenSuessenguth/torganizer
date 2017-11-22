package org.cc.torganizer.core.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MatchGetWinnerTest {

	private static final String HOME_NAME = "home";
	private static final String GUEST_NAME = "guest";

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
		    // Gastmannschaft gewinnt
		    { 0, 1, true, true, GUEST_NAME },
		    // Heimmannschaft gewinnt
		    { 1, 0, true, true, HOME_NAME },
		    // Spiel ist noch nicht beendet
		    { 1, 0, false, false, null },
		    // Spiel ist unentschieden ausgegangen
		    { 1, 1, true, false, null }, });
	}

	@Parameter(value = 0)
	public int homeScore;
	@Parameter(value = 1)
	public int guestScore;
	@Parameter(value = 2)
	public boolean isFinished;
	@Parameter(value = 3)
	public boolean matchHasWinner;
	@Parameter(value = 4)
	public String nameOfWinner;

	private Match match;

	@Before
	public void before() {
		Opponent home = new Player(HOME_NAME, HOME_NAME);
		Opponent guest = new Player(GUEST_NAME, GUEST_NAME);
		match = new Match(home, guest);
		if (isFinished) {
			match.setFinishedTime(LocalDateTime.now());
		}
		Result result = new Result(0, homeScore, guestScore);
		match.addResult(result);

	}

	@Test
	public void testGetWinner() {
		if (match.isFinished() && matchHasWinner) {
			assertThat(match.getWinner(), is(notNullValue()));
			assertThat(match.getWinner().getPlayers().get(0).getPerson().getFirstName(), is(nameOfWinner));
		} else {
			MatcherAssert.assertThat(match.getWinner(), Matchers.is(Matchers.instanceOf(Unknown.class)));
		}
	}
}
