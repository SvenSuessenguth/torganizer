/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.core.entities;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author svens
 */
public class SquadTest {
  
  private Squad squad;
  
  @BeforeEach
  public void before(){
    squad = new Squad();
  }

  @Test
  public void testAddPlayers() {
    Collection<Player> players = Arrays.asList(new Player(), new Player());
    squad.addPlayers(players);
    MatcherAssert.assertThat(squad.getPlayers(), Matchers.hasSize(2));
  }  
}
