package org.cc.torganizer.frontend.squads.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Squad;

@RequestScoped
@Named
public class SelectSquad extends SquadsAction {

  public void execute(Squad selected) {
    state.setCurrent(selected);
  }
}
