package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class CancelPlayer extends PlayersAction {

    /**
     * creating a player without persisting it.
     */
    public String execute() {
        Player player = new Player(new Person());
        state.setCurrent(player);

        return null;
    }
}
