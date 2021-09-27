/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author svens
 */
class SquadTest {

  private Squad squad;

  @BeforeEach
  void before() {
    squad = new Squad();
  }

  @Test
  void testAddPlayers() {
    Collection<Player> players = Arrays.asList(new Player(new Person()), new Player(new Person()));
    squad.addPlayers(players);

    assertThat(squad.getPlayers()).hasSize(2);
  }

  @Test
  void testAddPlayer_playerNull() {
    assertThatThrownBy(() -> squad.addPlayer(null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testAddPlayer_playersPersonNull() {
    Player player = new Player();
    assertThatThrownBy(() -> squad.addPlayer(player)).isInstanceOf(IllegalArgumentException.class);
  }
}
