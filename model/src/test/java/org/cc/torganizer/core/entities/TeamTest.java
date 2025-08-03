package org.cc.torganizer.core.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TeamTest {

  private Team team;

  @BeforeEach
  void beforeEach() {
    team = new Team();
  }

  @Test
  void getPlayers() {
    var squad = new Squad();
    squad.addPlayer(new Player(new Person("1", "1")));
    squad.addPlayer(new Player(new Person("2", "2")));
    team.addOpponent(squad);

    var players = team.getPlayers();

    assertThat(players).hasSize(2);
  }
}