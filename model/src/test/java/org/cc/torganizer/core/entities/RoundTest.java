package org.cc.torganizer.core.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RoundTest {

  @Test
  void getGroup_positionIsNotEqualToIndex() {
    Round round = new Round();
    round.addGroup(new Group(1));
    round.addGroup(new Group(3));
    round.addGroup(new Group(0));
    round.addGroup(new Group(2));

    for(Integer position = 0 ; position<=3 ; position+=1){
      Group group = round.getGroup(position);
      assertThat(group.getPosition(), is(position));
    }
  }

  @Test
  public void addGroup_positionAlreadyInList(){
    Round round = new Round();
    round.addGroup(new Group(1));
    round.addGroup(new Group(3));
    round.addGroup(new Group(2));

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      round.addGroup(new Group(1));
    });
  }

  @Test
  public void addGroup_groupHasPosition(){
    Round round = new Round();
    round.addGroup(new Group(1));

    assertThat(round.getGroups(), hasSize(1));
  }

  @Test
  public void addGroup_groupHasNoPosition(){
    Round round = new Round();
    Group group = new Group();
    round.addGroup(group);

    assertThat(group.getPosition(), is(0));
  }
}