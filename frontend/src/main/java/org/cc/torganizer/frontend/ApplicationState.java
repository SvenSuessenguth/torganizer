package org.cc.torganizer.frontend;

import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Named;
import lombok.Data;
import org.cc.torganizer.core.entities.Tournament;

import java.io.Serializable;

/**
 * State of the Tournament-Application.
 */
@ConversationScoped
@Named
@Data
public class ApplicationState implements Serializable {

  private Tournament tournament;

  public Long getTournamentId() {
    return tournament.getId();
  }
}
