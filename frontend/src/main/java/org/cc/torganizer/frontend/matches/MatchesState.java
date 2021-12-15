package org.cc.torganizer.frontend.matches;

import jakarta.enterprise.inject.Vetoed;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.entities.Match;

/**
 * State for the match-view.
 */
@Vetoed
public class MatchesState implements Serializable {

  private List<Match> finishedMatches = new ArrayList<>();
  private List<Match> possibleMatches = new ArrayList<>();
  private List<Match> runningMatches = new ArrayList<>();

  public List<Match> getRunningMatches() {
    return runningMatches;
  }

  public void setRunningMatches(List<Match> runningMatches) {
    this.runningMatches = runningMatches;
  }

  public List<Match> getPossibleMatches() {
    return possibleMatches;
  }

  public void setPossibleMatches(List<Match> possibleMatches) {
    this.possibleMatches = possibleMatches;
  }

  public List<Match> getFinishedMatches() {
    return this.finishedMatches;
  }

  public void setFinishedMatches(List<Match> finishedMatches) {
    this.finishedMatches = finishedMatches;
  }
}
