package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class SynchronizePlayerState extends PlayersAction {

    /**
     * synchronizing the state with database.
     */
    public String execute() {
        if (applicationState.getTournament() == null || applicationState.getTournamentId() == null) {
            return "tournament";
        }

        state.synchronize();
        return null;
    }
}
