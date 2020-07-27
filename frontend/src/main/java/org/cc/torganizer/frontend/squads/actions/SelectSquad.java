package org.cc.torganizer.frontend.squads.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.cc.torganizer.core.entities.Squad;

@RequestScoped
@Named
public class SelectSquad extends SquadsAction {

    public void execute(Squad selected) {
        state.setCurrent(selected);
    }
}
