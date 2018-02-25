/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.core.entities;

import java.util.Arrays;
import java.util.Collection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author svens
 */
public class SquadTest {
  
  private Squad squad;
  
  @Before
  public void before(){
    squad = new Squad();
  }

  /**
   * Test of addPlayers method, of class Squad.
   */
  @Test
  public void testAddPlayers() {
    Collection<Player> players = Arrays.asList(new Player(), new Player());
    squad.addPlayers(players);
    MatcherAssert.assertThat(squad.getPlayers(), Matchers.hasSize(2));
  }  
}
