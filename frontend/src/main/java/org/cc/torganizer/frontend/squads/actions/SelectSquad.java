package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Squad;

/**
 * Selecting a Squad in the UI.
 */
@RequestScoped
@Named
@SuppressWarnings("unused")
public class SelectSquad extends SquadAction {
  public void execute(Squad selected) {
    state.setCurrent(selected);
  }
}
