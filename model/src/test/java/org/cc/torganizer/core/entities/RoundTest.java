package org.cc.torganizer.core.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

class RoundTest {

  @Test
  void getGroup_positionIsNotEqualToIndex() {
    Round round = new Round();
    round.appendGroup(new Group(1));
    round.appendGroup(new Group(3));
    round.appendGroup(new Group(0));
    round.appendGroup(new Group(2));

    for(Integer position = 0 ; position<=3 ; position+=1){
      Group group = round.getGroup(position);
      assertThat(group.getPosition(), is(position));
    }
  }

  @Test
  public void appendGroup_everyGroupHasPosition(){
    Round round = new Round();
    Group group = new Group();

    round.appendGroup(group);

    assertThat(group.getPosition(), is(not(nullValue())));
  }

  @Test
  public void appendGroup_appendAtEnd(){
    Round round = new Round();
    round.appendGroup(new Group());
    round.appendGroup(new Group());
    round.appendGroup(new Group());

    Group group = new Group();
    group.setPosition(1);

    round.appendGroup(group);

    assertThat(group.getPosition(), is(3));
  }

  @Test
  public void appendGroup_groupHasPosition(){
    Round round = new Round();
    round.appendGroup(new Group(1));

    assertThat(round.getGroups(), hasSize(1));
  }

  @Test
  public void appendGroup_groupHasNoPosition(){
    Round round = new Round();
    Group group = new Group();
    round.appendGroup(group);

    assertThat(group.getPosition(), is(0));
  }
}