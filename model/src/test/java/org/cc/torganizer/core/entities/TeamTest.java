package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeamTest {

  private Team team;

  @BeforeEach
  public void beforeEach() {
    team = new Team();
  }

  @Test
  void getPlayers() {
    Squad squad = new Squad();
    squad.addPlayer(new Player(new Person()));
    squad.addPlayer(new Player(new Person()));
    team.addOpponent(squad);

    Set<Player> players = team.getPlayers();

    assertThat(players).hasSize(2);
  }
}