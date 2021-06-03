package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class RoundTest {

  @Test
  void getGroup_noGroupPresent() {
    Round round = new Round();

    Group group = round.getGroup(1);
    assertThat(group).isNull();
  }

  @Test
  void getGroup_positionIsNotEqualToIndex() {
    Round round = new Round();
    round.appendGroup(new Group(1));
    round.appendGroup(new Group(3));
    round.appendGroup(new Group(0));
    round.appendGroup(new Group(2));

    for (int position = 0; position <= 3; position += 1) {
      Group group = round.getGroup(position);
      assertThat(group.getPosition()).isEqualTo(position);
    }
  }

  @Test
  void appendGroup_everyGroupHasPosition() {
    Round round = new Round();
    Group group = new Group();

    round.appendGroup(group);

    assertThat(group.getPosition()).isNotNull();
  }

  @Test
  void appendGroup_appendAtEnd() {
    Round round = new Round();
    round.appendGroup(new Group());
    round.appendGroup(new Group());
    round.appendGroup(new Group());

    Group group = new Group();
    group.setPosition(1);

    round.appendGroup(group);

    assertThat(group.getPosition()).isEqualTo(3);
  }

  @Test
  void appendGroup_groupHasPosition() {
    Round round = new Round();
    round.appendGroup(new Group(1));

    assertThat(round.getGroups()).hasSize(1);
  }

  @Test
  void appendGroup_groupHasNoPosition() {
    Round round = new Round();
    Group group = new Group();
    round.appendGroup(group);

    assertThat(group.getPosition()).isZero();
  }

  @Test
  void getDeletabelGroups_allGroupsHaveOpponents() {
    Round round = new Round();
    Group group1 = new Group();
    group1.addOpponent(new Player());
    round.appendGroup(group1);
    Group group2 = new Group();
    group2.addOpponent(new Player());
    round.appendGroup(group2);

    List<Group> deletableGroups = round.getDeletableGroups();

    assertThat(deletableGroups).isEmpty();
  }

  @Test
  void getDeletabelGroups_atLeastOneGroupsHasNoOpponents() {
    Round round = new Round();
    Group group1 = new Group();
    group1.addOpponent(new Player());
    round.appendGroup(group1);
    Group group2 = new Group();
    round.appendGroup(group2);

    List<Group> deletableGroups = round.getDeletableGroups();

    assertThat(deletableGroups).hasSize(1);
  }

  @Test
  void getDeletabelGroups_noGroupAssigned() {
    Round round = new Round();

    List<Group> deletableGroups = round.getDeletableGroups();

    assertThat(deletableGroups).isEmpty();
  }
}