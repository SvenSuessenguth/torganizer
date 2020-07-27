package org.cc.torganizer.frontend.squads.actions;

import static org.cc.torganizer.frontend.squads.SquadsState.CURRENT_SQUAD_PLAYERS_TABLE_SIZE;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

/**
 * Removing a player from the current squad.
 */
@RequestScoped
@Named
public class RemovePlayerFromSquad extends SquadsAction {

    /**
     * Functional Interface Methode.
     */
    public void execute(Player player) {

        Squad current = state.getCurrent();
        current.removePlayer(player);

        // to show table, add empty players with no id, which are replaced
        // when actual players are added
        for (int i = 0; i < CURRENT_SQUAD_PLAYERS_TABLE_SIZE - current.getPlayers().size(); i++) {
            current.addPlayer(new Player(new Person()));
        }
    }
}
