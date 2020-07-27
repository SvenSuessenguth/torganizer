package org.cc.torganizer.frontend.squads.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class SynchronizeSquadsStateAction extends SquadsAction {

    /**
     * synchronizing the state with the database.
     */
    public String execute() {
        if (applicationState.getTournament() == null || applicationState.getTournamentId() == null) {
            return "tournament";
        }

        state.synchronize();
        return null;
    }
}
