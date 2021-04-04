package org.cc.torganizer.core;

import jakarta.enterprise.context.RequestScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;

/**
 * Assign Opponents evenly distibuted by Clubs to Groups by the RoundRobin-System.
 */
@RequestScoped
public class OpponentsToGroupsAssigner {

  public void assign(Set<Opponent> opponents, List<Group> groups) {
    // early exit
    if (opponents == null || opponents.isEmpty() || groups == null || groups.isEmpty()) {
      return;
    }

    // avoid/minimize matches with opponents of the same club
    for (Opponent opponent : opponents) {
      List<Group> minOpponentGroups = getGroupsWithMinOpponents(groups);

      if (minOpponentGroups.size() == 1) {
        minOpponentGroups.get(0).addOpponent(opponent);
      } else {
        List<Club> clubs = opponent
            .getPlayers()
            .stream()
            .map(Player::getClub)
            .collect(Collectors.toList());
        List<Group> minClubGroups = getGroupsWithMinClubMembers(minOpponentGroups, clubs);
        minClubGroups.get(0).addOpponent(opponent);
      }
    }
  }

  protected List<Group> getGroupsWithMinOpponents(List<Group> groups) {
    Integer minGroupsSize = Integer.MAX_VALUE;
    List<Group> groupsWithMinOppoents = new ArrayList<>();

    for (Group group : groups) {
      Integer groupSize = group.getOpponents().size();
      if (groupSize < minGroupsSize) {
        minGroupsSize = groupSize;
        groupsWithMinOppoents.clear();
      }
      if (groupSize.equals(minGroupsSize)) {
        groupsWithMinOppoents.add(group);
      }
    }

    return groupsWithMinOppoents;
  }

  protected List<Group> getGroupsWithMinClubMembers(List<Group> groups, List<Club> clubs) {
    List<Group> minClubMembers = new ArrayList<>();

    // counting clubs in group, which have to be minimized
    Map<Group, Integer> groupClubCount = new HashMap<>();
    for (Group group : groups) {
      groupClubCount.put(group, 0);
      for (Opponent o : group.getOpponents()) {
        for (Player p : o.getPlayers()) {
          if (clubs.contains(p.getClub())) {
            Integer tmpCounter = groupClubCount.get(group);
            groupClubCount.put(group, tmpCounter + 1);
          }
        }
      }
    }

    Integer minAccordance = Integer.MAX_VALUE;
    for (Map.Entry<Group, Integer> entry : groupClubCount.entrySet()) {
      Group group = entry.getKey();
      Integer count = entry.getValue();

      if (count < minAccordance) {
        minClubMembers.clear();
        minClubMembers.add(group);
        minAccordance = count;
      } else if (count == minAccordance) {
        minClubMembers.add(group);
      }
    }

    return minClubMembers;
  }
}
