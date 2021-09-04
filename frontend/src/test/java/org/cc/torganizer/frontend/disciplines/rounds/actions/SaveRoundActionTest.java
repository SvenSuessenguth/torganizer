package org.cc.torganizer.frontend.disciplines.rounds.actions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Round;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaveRoundActionTest {

  @InjectMocks
  private SaveRoundAction action;

  public static Stream<Arguments> createNecessaryGroups() {
    return Stream.of(
        Arguments.of(createRoundWithGroups(0), 1, 1),
        Arguments.of(createRoundWithGroups(1), 1, 1),
        Arguments.of(createRoundWithGroups(2), 1, 2)
    );
  }

  @ParameterizedTest
  @MethodSource
  void createNecessaryGroups(Round round, int newGroupsCount, int expectedSize) {
    action.createNecessaryGroups(round, newGroupsCount);

    assertThat(round.getGroups()).hasSize(expectedSize);
  }

  public static Stream<Arguments> deleteSuperfluousGroups() {
    return Stream.of(
        Arguments.of(createRoundWithGroups(0), 1, 0),
        Arguments.of(createRoundWithGroups(1), 1, 1),
        Arguments.of(createRoundWithGroups(2), 1, 1)
    );
  }

  @ParameterizedTest
  @MethodSource
  void deleteSuperfluousGroups(Round round, int newGroupsCount, int expectedSize) {
    action.deleteSuperfluousGroups(round, newGroupsCount);
    assertThat(round.getGroups()).hasSize(expectedSize);
  }

  private static Round createRoundWithGroups(int groupsCount) {
    Round round = new Round();
    for (int i = 0; i < groupsCount; i++) {
      round.appendGroup(new Group());
    }

    return round;
  }
}