package org.cc.torganizer.frontend.matches;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.PendingMatchDetectorFactory;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.persistence.MatchesRepository;

/**
 * State for the match-view.
 */
@RequestScoped
@Named
@SuppressWarnings("unused")
public class MatchesState {

  @Inject
  private MatchesRepository matchesRepository;

  @Inject
  private ApplicationState applicationState;

  @Inject
  private PendingMatchDetectorFactory pendingMatchDetectorFactory;

  public List<Match> getRunningMatches() {
    var tournament = applicationState.getTournament();
    return matchesRepository.getRunningMatches(tournament);
  }


  /**
   * Reading possible matches.
   */
  public List<Match> getPossibleMatches() {
    var possibleMatches = new ArrayList<Match>();

    var tournament = applicationState.getTournament();
    for (var d : tournament.getDisciplines()) {
      for (var r : d.getRounds()) {
        var system = r.getSystem();
        var pendingMatchDetector = pendingMatchDetectorFactory.getPendingMatchDetector(system);
        for (var g : r.getGroups()) {
          var pendingMatches = pendingMatchDetector.getPendingMatches(g);
          possibleMatches.addAll(pendingMatches);
        }
      }
    }


    return possibleMatches;
  }

  public List<Match> getFinishedMatches() {
    var tournament = applicationState.getTournament();
    return matchesRepository.getFinishedMatches(tournament);
  }
}
