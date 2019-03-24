package org.cc.torganizer.rest.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import javax.json.JsonObject;

import org.assertj.core.api.Assertions;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.PositionalOpponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PositionalOpponentJsonConverterTest {

  @Mock
  private OpponentJsonConverterProvider provider;

  @InjectMocks
  private PositionalOpponentJsonConverter converter;

  @BeforeEach
  void beforeEach() {
    PersonJsonConverter personConverter = new PersonJsonConverter();
    ClubJsonConverter clubConverter = new ClubJsonConverter();

    when(provider.getConverter(PLAYER)).thenReturn(new PlayerJsonConverter(personConverter, clubConverter));
  }

  @Test
  void testPositionalPlayerToJsonObject() {
    Person person = new Person("vn", "nn");
    Player player = new Player(person);
    int position = 1;
    PositionalOpponent po = new PositionalOpponent(player, position);

    String expected = "{\"id\":null," +
        "\"lastMatch\":null," +
        "\"status\":\"ACTIVE\"," +
        "\"person\":{\"id\":null," +
        "\"firstName\":\"vn\"," +
        "\"lastName\":\"nn\"," +
        "\"dateOfBirth\":null," +
        "\"gender\":\"UNKNOWN\"}," +
        "\"club\":{\"id\":null," +
        "\"name\":null}," +
        "\"position\":1}";

    JsonObject jsonObject = converter.toJsonObject(po);

    assertThat(jsonObject).asString().isEqualTo(expected);
  }
}