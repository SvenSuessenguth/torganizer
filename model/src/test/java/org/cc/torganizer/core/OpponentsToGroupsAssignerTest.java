package org.cc.torganizer.core;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

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
    var c1 = new Club("c1");
    var c2 = new Club("c2");
    var opponents = new HashSet<Opponent>();
    var o1 = new Player("o1", "o1", c1);
    opponents.add(o1);
    var o2 = new Player("o2", "o2", null);
    opponents.add(o2);
    var o3 = new Player("o3", "o3", c1);
    opponents.add(o3);
    var o4 = new Player("o4", "o4", c1);
    opponents.add(o4);
    var o5 = new Player("o5", "o5", c2);
    opponents.add(o5);
    var o6 = new Player("o6", "o6", c2);
    opponents.add(o6);
    var o7 = new Player("o7", "o7", null);
    opponents.add(o7);

    var groups = new ArrayList<Group>();
    groups.add(new Group());
    groups.add(new Group());

    assigner.assign(opponents, groups);

    var opponents0 = groups.getFirst().getOpponents();
    var opponents1 = groups.get(1).getOpponents();
    assertThat(opponents0).hasSizeBetween(3, 4);
    assertThat(opponents1).hasSizeBetween(3, 4);
    assertThat(opponents0.size() + opponents1.size()).isEqualTo(7);
  }

  @Test
  void testAssignOpponentsToNullGroups() {
    var opponents = new HashSet<Opponent>();
    var o1 = new Player("o1", "o1", null);
    opponents.add(o1);

    var groups = new ArrayList<Group>();

    assigner.assign(opponents, groups);

    assertThat(groups).isEmpty();
  }


  static Stream<Arguments> testGetGroupsWithMinOpponents() {
    return Stream.of(
      Arguments.of(asList(2, 2, 3, 2, 3), asList(0, 1, 3)),
      Arguments.of(asList(1, 2, 3), singletonList(0)),
      Arguments.of(asList(2, 1, 3), singletonList(1))
    );
  }

  @ParameterizedTest
  @MethodSource
  void testGetGroupsWithMinOpponents(List<Integer> opponentsPerGroup, List<Integer> expectedGroupsIndexes) {
    // creating groups
    var groups = new ArrayList<Group>();
    for (var i : opponentsPerGroup) {
      var g = new Group();
      groups.add(g);
      for (var counter = 0; counter < i; counter++) {
        g.addOpponent(new Player("", ""));
      }
    }

    var groupsWithMinOpponents = assigner.getGroupsWithMinOpponents(groups);

    assertThat(groupsWithMinOpponents).hasSize(expectedGroupsIndexes.size());


    for (var i = 0; i < groupsWithMinOpponents.size(); i++) {
      var g = groupsWithMinOpponents.get(i);
      var index = expectedGroupsIndexes.get(i);

      assertThat(groups.indexOf(g)).isEqualTo(index);
    }
  }

  static Stream<Arguments> testGetGroupsWithMinClubMembers() {
    var c1 = new Club("c1");
    var c2 = new Club("c2");
    var c3 = new Club("c3");
    Club c4 = null;

    return Stream.of(
      Arguments.of(
        asList(group(1L, opponent(c1), opponent(c1)),
          group(2L, opponent(c4), opponent(c1))),
        asList(c1, c2),
        singletonList(2L)),
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
        singletonList(2L)),
      Arguments.of(
        asList(group(1L, opponent(c1), opponent(c2)),
          group(2L, opponent(c3), opponent(c3))),
        singletonList(c1),
        singletonList(2L))

    );
  }

  @ParameterizedTest
  @MethodSource
  void testGetGroupsWithMinClubMembers(List<Group> groups, List<Club> clubs, List<Long> expectedIds) {
    var groupsWithMinClubMembers = assigner.getGroupsWithMinClubMembers(groups, clubs);

    assertThat(groupsWithMinClubMembers).hasSize(expectedIds.size());

    var actualIds = new ArrayList<>();
    for (var g : groupsWithMinClubMembers) {
      actualIds.add(g.getId());
    }
    // https://stackoverflow.com/questions/43056202/how-to-assert-that-two-listsstring-are-equal-ignoring-order
    assertThat(actualIds).containsExactlyInAnyOrder(expectedIds.toArray(new Long[]{}));
  }

  static Group group(Long id, Opponent... opponents) {
    var g = new Group();
    g.setId(id);
    for (var o : opponents) {
      g.addOpponent(o);
    }

    return g;
  }

  static Opponent opponent(Club c) {
    var player = new Player("", "");
    player.setClub(c);
    return player;
  }
}