package org.cc.torganizer.frontend.squads;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.cc.torganizer.core.comparators.player.PlayerByLastNameComparator;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Player;

/**
 * Backing for editing Squads.
 */
@RequestScoped
@Named
public class SquadsBacking {

  @Inject
  private SquadsState squadsState;

  /**
   * Getting the players ordered by the lastName.
   */
  public Collection<Player> getCurrentSquadPlayersOrderedByLastName() {

    var players = squadsState.getCurrent().getPlayers();

    // order players by lastname
    var orderedPlayers = new ArrayList<>(players);
    orderedPlayers.sort(new PlayerByLastNameComparator());
    Collections.reverse(orderedPlayers);

    return orderedPlayers;
  }

  public Gender[] getGenders() {
    return Gender.values();
  }
}
