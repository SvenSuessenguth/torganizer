package org.cc.torganizer.frontend;

import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.Logger;

/**
 * Start and ends a conversation.
 */
@Named
@RequestScoped
public class ConversationController {

  @Inject
  private Logger logger;

  @Inject
  private Conversation conversation;

  @Inject
  private FacesContext facesContext;

  /**
   * starts a conversation.
   */
  public void beginConversation() {
    if (conversation.isTransient()) {
      conversation.begin();
      logger.info("conversation begin with id '{}'", conversation.getId());
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
