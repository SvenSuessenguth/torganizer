package org.cc.torganizer.frontend.clubs.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.frontend.ApplicationMessages;

@RequestScoped
@Named
public class DeleteClub extends ClubsAction {

    public static final String CLUBS_I18N_BASE_NAME = "org.cc.torganizer.frontend.clubs";

    @Inject
    private ApplicationMessages appMessages;

    /**
     * A club can only be deleted if it has no linked players.
     */
    public void execute() {
        Club current = state.getCurrent();
        Long id = current.getId();

        Long clubsPlayers = clubsRepository.countPlayers(current);

        if (clubsPlayers > 0) {
            appMessages.addMessage(CLUBS_I18N_BASE_NAME, "no_delete_linked_players",
                    new Object[]{current.getName()});
        } else {
            clubsRepository.delete(id);
            state.synchronize();
        }
    }
}
