package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

@ExtendWith(MockitoExtension.class)
public class DisciplineJsonConverterTest {

  @Spy
  private RestrictionJsonConverter restrictionConverter;

  @InjectMocks
  private DisciplineJsonConverter converter;

  @Test
  public void testToJsonObject() {
    String expected = "{\"id\":null,\"name\":\"Damen-Einzel U50\","
      + "\"restrictions\":"
      + "[{\"id\":null,\"discriminator\":\"A\",\"minDateOfBirth\":\"1968-01-12\",\"maxDateOfBirth\":\"1970-01-12\"},"
      + "{\"id\":null,\"discriminator\":\"G\",\"gender\":\"FEMALE\"},"
      + "{\"id\":null,\"discriminator\":\"O\",\"opponentType\":\"PLAYER\"}]}";

    Discipline discipline = new Discipline();
    discipline.setName("Damen-Einzel U50");

    AgeRestriction ageRestriction = new AgeRestriction();
    ageRestriction.setMinDateOfBirth(LocalDate.of(1968, Month.JANUARY, 12));
    ageRestriction.setMaxDateOfBirth(LocalDate.of(1970, Month.JANUARY, 12));
    discipline.addRestriction(ageRestriction);

    GenderRestriction genderRestriction = new GenderRestriction();
    genderRestriction.setGender(Gender.FEMALE);
    discipline.addRestriction(genderRestriction);

    OpponentTypeRestriction opponentTypeRestriction = new OpponentTypeRestriction();
    opponentTypeRestriction.setOpponentType(OpponentType.PLAYER);
    discipline.addRestriction(opponentTypeRestriction);

    String actual = converter.toJsonObject(discipline).toString();

    MatcherAssert.assertThat(actual, Matchers.is(expected));
  }
}
