package org.cc.torganizer.frontend.players;

import java.io.Serializable;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ConversationScoped
public class PlayersState implements Serializable {
  private static final long serialVersionUID = 3683970655136738688L;

  @Inject
  private Conversation conversation;

  public String getConversationId(){
    return conversation.getId();
  }
}
