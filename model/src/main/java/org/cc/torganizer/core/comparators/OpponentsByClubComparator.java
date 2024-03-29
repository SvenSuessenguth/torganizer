package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;

/**
 * Comparing Opponents by Clubs.
 */
public class OpponentsByClubComparator implements Comparator<Opponent>, Serializable {

  @Override
  public int compare(Opponent o1, Opponent o2) {

    if (Objects.equals(o1, o2)) {
      return 0;
    }
    if (o1 == null) {
      return 1;
    } else if (o2 == null) {
      return -1;
    }


    var club1 = getOpponentsClub(o1);
    var club2 = getOpponentsClub(o2);

    return new ClubComparator().compare(club1, club2);
  }

  protected Club getOpponentsClub(Opponent opponent) {

    if (opponent == null || opponent.getPlayers().isEmpty()) {
      return null;
    }

    // list all Clubnames of the Players
    // Players can play together as in squads, whether they are in different clubs
    var clubs = new ArrayList<Club>();
    for (Player player : opponent.getPlayers()) {
      var club = player.getClub();
      if (club != null) {
        clubs.add(club);
      }
    }
    clubs.sort(new ClubComparator());


    return clubs.isEmpty() ? null : clubs.get(0);
  }
}
