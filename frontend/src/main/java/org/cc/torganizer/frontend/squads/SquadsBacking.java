package org.cc.torganizer.frontend.squads;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.cc.torganizer.core.comparators.player.PlayerByLastNameComparator;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class SquadsBacking {
  @Inject
  private SquadsState squadsState;

  /**
   * Getting the players ordered by the lastName.
   */
  public Collection<Player> getCurrentSquadPlayersOrderedByLastName() {

    Collection<Player> players = squadsState.getCurrent().getPlayers();

    // order players by lastname
    List<Player> orderedPlayers = new ArrayList<>(players);
    Collections.sort(orderedPlayers, new PlayerByLastNameComparator());
    Collections.reverse(orderedPlayers);

    return orderedPlayers;
  }
}
