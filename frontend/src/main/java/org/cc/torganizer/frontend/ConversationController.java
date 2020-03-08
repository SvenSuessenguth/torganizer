package org.cc.torganizer.frontend;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.frontend.logging.SimplifiedLoggerFacade;
import org.cc.torganizer.frontend.logging.online.Online;

@Named
@RequestScoped
public class ConversationController {

  @Inject
  @Online
  private SimplifiedLoggerFacade log;

  @Inject
  private Conversation conversation;

  @Inject
  private FacesContext facesContext;

  /**
   * init conversation on starting to edit the 'content' of a selected tournament.
   */
  public void initConversation() {
    if (conversation.isTransient()) {
      conversation.begin();
      log.info("conversation begin with id " + conversation.getId());
    }
  }

  /**
   * end of editing a tournaments 'content'.
   */
  public void endConversation() {
    if (!facesContext.isPostback() && !conversation.isTransient()) {
      conversation.end();
    }
  }
}
