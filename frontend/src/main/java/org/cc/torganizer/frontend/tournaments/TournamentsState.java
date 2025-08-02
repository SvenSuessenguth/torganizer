package org.cc.torganizer.frontend.tournaments;

import jakarta.enterprise.inject.Vetoed;
import lombok.Data;
import org.cc.torganizer.core.entities.Tournament;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The current tournament is used for every following action. So this bean is in session-scope.
 */
@Vetoed
@Data
public class TournamentsState implements Serializable {
  private List<Tournament> tournaments = new ArrayList<>();
  private Tournament current;
}
