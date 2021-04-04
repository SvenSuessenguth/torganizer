package org.cc.torganizer.core;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OpponentsToGroupsAssignerTest {

  private OpponentsToGroupsAssigner assigner;

  @BeforeEach
  void beforeEach() {
    assigner = new OpponentsToGroupsAssigner();
  }

  @AfterEach
  void afterEach() {
    assigner = null;
  }

  @Test
  void assignOpponentsToGroups() {
    Club c1 = new Club("c1");
    Club c2 = new Club("c2");
    Set<Opponent> opponents = new HashSet<>();
    Player o1 = new Player("o1", "o1", c1);
    opponents.add(o1);
    Player o2 = new Player("o2", "o2", null);
    opponents.add(o2);
    Player o3 = new Player("o3", "o3", c1);
    opponents.add(o3);
    Player o4 = new Player("o4", "o4", c1);
    opponents.add(o4);
    Player o5 = new Player("o5", "o5", c2);
    opponents.add(o5);
    Player o6 = new Player("o6", "o6", c2);
    opponents.add(o6);
    Player o7 = new Player("o7", "o7", null);
    opponents.add(o7);

    List<Group> groups = new ArrayList<>();
    groups.add(new Group());
    groups.add(new Group());

    assigner.assign(opponents, groups);

    assertThat(groups.get(0).getOpponents()).hasSize(4);
    assertThat(groups.get(1).getOpponents()).hasSize(3);
  }

  @Test
  void testAssignOpponentsToNullGroups() {
    Set<Opponent> opponents = new HashSet<>();
    Player o1 = new Player("o1", "o1", null);
    opponents.add(o1);

    List<Group> groups = new ArrayList<>();

    assigner.assign(opponents, groups);

    assertThat(groups).isEmpty();
  }


  static Stream<Arguments> testGetGroupsWithMinOpponentsArguments() {
    return Stream.of(
        Arguments.of(asList(2, 2, 3, 2, 3), asList(0, 1, 3)),
        Arguments.of(asList(1, 2, 3), asList(0)),
        Arguments.of(asList(2, 1, 3), asList(1))
    );
  }

  @ParameterizedTest
  @MethodSource(value = "testGetGroupsWithMinOpponentsArguments")
  void testGetGroupsWithMinOpponents(List<Integer> opponentsPerGroup, List<Integer> expectedGroupsIndexes) {
    // creating groups
    List<Group> groups = new ArrayList<>();
    for (Integer i : opponentsPerGroup) {
      Group g = new Group();
      groups.add(g);
      for (int counter = 0; counter < i; counter++) {
        g.addOpponent(new Player("", ""));
      }
    }

    List<Group> groupsWithMinOpponents = assigner.getGroupsWithMinOpponents(groups);

    assertThat(groupsWithMinOpponents).hasSize(expectedGroupsIndexes.size());


    for (int i = 0; i < groupsWithMinOpponents.size(); i++) {
      Group g = groupsWithMinOpponents.get(i);
      int index = expectedGroupsIndexes.get(i);

      assertThat(groups.indexOf(g)).isEqualTo(index);
    }
  }

  static Stream<Arguments> testGetGroupsWithMinClubMembersArguments() {
    Club c1 = new Club("c1");
    Club c2 = new Club("c2");
    Club c3 = new Club("c3");
    Club c4 = null;

    return Stream.of(
        Arguments.of(
            asList(group(1L, opponent(c1), opponent(c1)),
                group(2L, opponent(c4), opponent(c1))),
            asList(c1, c2),
            asList(2L)),
        Arguments.of(
            asList(group(1L, opponent(c4), opponent(c1)),
                group(2L, opponent(c4), opponent(c1))),
            asList(c1, c2),
            asList(1L, 2L)),
        Arguments.of(
            asList(group(1L, opponent(c1), opponent(c1)),
                group(2L, opponent(c4), opponent(c1))),
            asList(c1, c4),
            asList(1L, 2L)),
        Arguments.of(
            asList(group(1L, opponent(c1), opponent(c1), opponent(c1)),
                group(2L, opponent(c4), opponent(c1))),
            asList(c1, c4),
            asList(2L))

    );
  }

  @ParameterizedTest
  @MethodSource(value = "testGetGroupsWithMinClubMembersArguments")
  void testGetGroupsWithMinClubMembers(List<Group> groups, List<Club> clubs, List<Long> expectedIds) {
    List<Group> groupsWithMinClubMembers = assigner.getGroupsWithMinClubMembers(groups, clubs);

    assertThat(groupsWithMinClubMembers).hasSize(expectedIds.size());

    List<Long> actualIds = new ArrayList<>();
    for (Group g : groupsWithMinClubMembers) {
      actualIds.add(g.getId());
    }
    // https://stackoverflow.com/questions/43056202/how-to-assert-that-two-listsstring-are-equal-ignoring-order
    assertThat(actualIds).containsExactlyInAnyOrder(expectedIds.toArray(new Long[expectedIds.size()]));
  }

  static Group group(Long id, Opponent... opponents) {
    Group g = new Group();
    g.setId(id);
    for (Opponent o : opponents) {
      g.addOpponent(o);
    }

    return g;
  }

  static Opponent opponent(Club c) {
    Player player = new Player("", "");
    player.setClub(c);
    return player;
  }
}