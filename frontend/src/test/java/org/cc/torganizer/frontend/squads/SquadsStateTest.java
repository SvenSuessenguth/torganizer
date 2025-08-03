package org.cc.torganizer.frontend.squads;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.Gender.UNKNOWN;
import static org.cc.torganizer.frontend.squads.SquadsState.ALL_PLAYERS_TABLE_SIZE;

class SquadsStateTest {

  private SquadsState squadsState;

  @BeforeEach
  void beforeEach() {
    squadsState = new SquadsState();
  }

  @SuppressWarnings("unused")
  public static Stream<Arguments> hasNextAllPlayersTableChunkArguments() {
    return Stream.of(
      Arguments.of(getAmountOfPlayers(0), 0, false),
      Arguments.of(getAmountOfPlayers(0), 1, false),
      Arguments.of(getAmountOfPlayers(0), 2, false),
      Arguments.of(getAmountOfPlayers(0), -1, false),
      Arguments.of(null, 1, false),
      Arguments.of(getAmountOfPlayers(10), 1, false),
      Arguments.of(getAmountOfPlayers(100), 10, false),
      Arguments.of(getAmountOfPlayers(100), 11, false),

      Arguments.of(getAmountOfPlayers(11), 0, true),
      Arguments.of(getAmountOfPlayers(101), 9, true),
      Arguments.of(getAmountOfPlayers(101), 8, true)
    );
  }

  @ParameterizedTest
  @MethodSource("hasNextAllPlayersTableChunkArguments")
  void hasNextAllPlayersTableChunk(List<Player> players, int allPlayersTableIndex, boolean expected) {
    squadsState.setPlayers(players);
    squadsState.setAllPlayersTableIndex(allPlayersTableIndex);

    var actual = squadsState.hasNextAllPlayersTableChunk();

    assertThat(actual).isEqualTo(expected);
  }

  @SuppressWarnings("unused")
  public static Stream<Arguments> getPlayersArguments() {
    return Stream.of(
      Arguments.of(null, MALE, 0, 0),
      Arguments.of(getAmountPlayers(0, MALE), MALE, 0, 0),
      Arguments.of(getAmountPlayers(1, MALE), MALE, 0, ALL_PLAYERS_TABLE_SIZE),
      Arguments.of(getAmountPlayers(9, MALE), MALE, 0, ALL_PLAYERS_TABLE_SIZE),
      Arguments.of(getAmountPlayers(10, MALE), MALE, 0, ALL_PLAYERS_TABLE_SIZE),
      Arguments.of(getAmountPlayers(1, MALE), MALE, 1, 0),
      Arguments.of(getAmountPlayers(1, MALE), FEMALE, 1, 0)
    );
  }

  @ParameterizedTest
  @MethodSource("getPlayersArguments")
  void getPlayers(List<Player> players, Gender gender, int allPlayersTableIndex, int expectedChunkSize) {
    squadsState.setPlayers(players);
    squadsState.setGender(gender);
    squadsState.setAllPlayersTableIndex(allPlayersTableIndex);

    var actualChunkSize = squadsState.getPlayers().size();

    assertThat(actualChunkSize).isEqualTo(expectedChunkSize);
  }

  private static List<Player> getAmountOfPlayers(Integer size) {
    return getAmountPlayers(size, UNKNOWN);
  }

  private static List<Player> getAmountPlayers(Integer size, Gender gender) {
    var players = new ArrayList<Player>();

    for (var index = 0; index < size; index++) {
      var player = new Player(String.valueOf(index), String.valueOf(index));
      player.getPerson().setGender(gender);
      players.add(player);
    }

    return players;
  }

  @Test
  void incAllPlayersTableChunk() {
    var index = 1;
    squadsState.setAllPlayersTableIndex(index);
    squadsState.incAllPlayersTableChunk();
    var actual = squadsState.getAllPlayersTableIndex();

    assertThat(actual).isEqualTo(index + 1);
  }

  @SuppressWarnings("unused")
  public static Stream<Arguments> decAllPlayersTableChunk() {
    return Stream.of(
      Arguments.of(0, 0),
      Arguments.of(1, 0),
      Arguments.of(-1, 0),
      Arguments.of(2, 1)
    );
  }

  @ParameterizedTest
  @MethodSource
  void decAllPlayersTableChunk(int index, int expected) {
    squadsState.setAllPlayersTableIndex(index);
    squadsState.decAllPlayersTableChunk();
    var actual = squadsState.getAllPlayersTableIndex();

    assertThat(actual).isEqualTo(expected);
  }
}