package org.cc.torganizer.core;

import jakarta.enterprise.context.RequestScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;

/**
 * Assign Opponents evenly distibuted by Clubs to Groups by the RoundRobin-System.
 */
@RequestScoped
public class OpponentsToGroupsAssigner {

  /**
   * Assigning an set of opponents to some groups.
   * Assigning is optimized to reduce matches between opponents of the same club.
   */
  public void assign(Set<Opponent> opponents, List<Group> groups) {
    // early exit
    if (opponents == null || opponents.isEmpty() || groups == null || groups.isEmpty()) {
      return;
    }

    // avoid/minimize matches with opponents of the same club
    for (var opponent : opponents) {
      var minOpponentGroups = getGroupsWithMinOpponents(groups);

      if (minOpponentGroups.size() == 1) {
        minOpponentGroups.get(0).addOpponent(opponent);
      } else {
        var clubs = opponent
            .getPlayers()
            .stream()
            .map(Player::getClub)
            .toList();
        var minClubGroups = getGroupsWithMinClubMembers(minOpponentGroups, clubs);
        minClubGroups.get(0).addOpponent(opponent);
      }
    }
  }

  protected List<Group> getGroupsWithMinOpponents(List<Group> groups) {
    var minGroupsSize = Integer.MAX_VALUE;
    var groupsWithMinOppoents = new ArrayList<Group>();

    for (var group : groups) {
      var groupSize = group.getOpponents().size();
      if (groupSize < minGroupsSize) {
        minGroupsSize = groupSize;
        groupsWithMinOppoents.clear();
      }
      if (Objects.equals(groupSize, minGroupsSize)) {
        groupsWithMinOppoents.add(group);
      }
    }

    return groupsWithMinOppoents;
  }

  protected List<Group> getGroupsWithMinClubMembers(List<Group> groups, List<Club> clubs) {
    var minClubMembers = new ArrayList<Group>();

    // counting clubs in group, which have to be minimized
    var groupClubCount = new HashMap<Group, Integer>();
    for (var group : groups) {
      groupClubCount.put(group, 0);
      for (var o : group.getOpponents()) {
        for (var p : o.getPlayers()) {
          if (clubs.contains(p.getClub())) {
            Integer tmpCounter = groupClubCount.get(group);
            groupClubCount.put(group, tmpCounter + 1);
          }
        }
      }
    }

    var minAccordance = Integer.MAX_VALUE;
    for (var entry : groupClubCount.entrySet()) {
      var group = entry.getKey();
      var count = entry.getValue();

      if (count < minAccordance) {
        minClubMembers.clear();
        minClubMembers.add(group);
        minAccordance = count;
      } else if (Objects.equals(count, minAccordance)) {
        minClubMembers.add(group);
      }
    }

    return minClubMembers;
  }
}
