package org.cc.torganizer.core.entities;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AgeRestrictionTest {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				// in der Altersbegrenzung
				{ 2010, 1, 1, 2009, 1, 1, 2009, 1, 2, FALSE },
				// in der Altersbegrenzung (Taggenau an der Untergrenze)
				{ 2010, 1, 1, 2009, 1, 1, 2009, 1, 1, FALSE },
				// in der Altersbegrenzung (Taggenau an der Obergrenze)
				{ 2010, 1, 1, 2009, 1, 1, 2010, 1, 1, FALSE },
				// zu alt (einen Tag)
				{ 2010, 1, 1, 2009, 1, 1, 2008, 12, 31, TRUE },
				// zu jung (einen Tag)
				{ 2010, 1, 1, 2009, 1, 1, 2010, 1, 2, TRUE },
				// kein Geburtstag angegeben
				{ 2010, 1, 1, 2009, 1, 1, null, null, null, FALSE },
				// keine Untergrenze angegeben
				{ 2010, 1, 1, null, null, null, 2009, 1, 1, FALSE }, });
	}

	private AgeRestriction restriction;

	@Parameter(0)
	public Integer maxAgeYearOfBirth;
	@Parameter(1)
	public Integer maxAgeMonthOfBirth;
	@Parameter(2)
	public Integer maxAgeDayOfBirth;
	@Parameter(3)
	public Integer minAgeYearOfBirth;
	@Parameter(4)
	public Integer minAgeMonthOfBirth;
	@Parameter(5)
	public Integer minAgeDayOfBirth;
	@Parameter(6)
	public Integer yearOfBirth;
	@Parameter(7)
	public Integer monthOfBirth;
	@Parameter(8)
	public Integer dayOfBirth;
	@Parameter(9)
	public boolean restricted;

	private LocalDate maxDateOfBirth;
	private LocalDate minDateOfBirth;
	private LocalDate dateOfBirth;

	@Before
	public void before() {
		if (maxAgeYearOfBirth != null && maxAgeMonthOfBirth != null && maxAgeDayOfBirth != null) {
			maxDateOfBirth = LocalDate.of(maxAgeYearOfBirth, maxAgeMonthOfBirth, maxAgeDayOfBirth);
		}
		if (minAgeYearOfBirth != null && minAgeMonthOfBirth != null && minAgeDayOfBirth != null) {
			minDateOfBirth = LocalDate.of(minAgeYearOfBirth, minAgeMonthOfBirth, minAgeDayOfBirth);
		}
		restriction = new AgeRestriction();
		restriction.setMaxDateOfBirth(maxDateOfBirth);
		restriction.setMinDateOfBirth(minDateOfBirth);

		if (yearOfBirth != null && monthOfBirth != null && dayOfBirth != null) {
			dateOfBirth = LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth);
		}
	}

	@Test
	public void testSomething() {
		Person person = new Person("firstName", "lastName");
		person.setDateOfBirth(dateOfBirth);
		Opponent opponent = new Player(person);

		boolean isRestricted = restriction.isRestricted(opponent);

		assertThat(isRestricted, is(restricted));
	}
}
