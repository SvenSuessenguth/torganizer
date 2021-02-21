package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.frontend.squads.SquadsState;

/**
 * Selecting a Squad in the UI.
 */
@RequestScoped
@Named
@SuppressWarnings("unused")
public class SelectSquad {

  @Inject
  protected SquadsState state;

  public void execute(Squad selected) {
    state.setCurrent(selected);
  }
}
