package org.cc.torganizer.frontend.tournaments;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ConversationController;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Synchronizing the tournaments state with db-data.
 */
@RequestScoped
public class TournamentsStateSynchronizer {

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private ConversationController conversationController;

  /**
   * Synchronizing the tournaments state with db-data.
   */
  public void synchronize(TournamentsState state) {
    List<Tournament> tournaments = tournamentsRepository.read(0, 100);
    Tournament current = new Tournament();

    state.setTournaments(tournaments);
    state.setCurrent(current);

    conversationController.beginConversation();
  }
}
