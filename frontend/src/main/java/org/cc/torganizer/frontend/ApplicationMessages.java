package org.cc.torganizer.frontend;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Adding Messages to the FacesContext.
 */
@RequestScoped
public class ApplicationMessages {

  @Inject
  private FacesContext facesContext;

  public void addMessage(String baseName, String key, Object[] arguments) {
    addMessage(null, baseName, key, arguments);
  }

  /**
   * Add FacesMessage to context.
   */
  public void addMessage(String clientId, String baseName, String key, Object[] arguments) {
    var locale = facesContext.getViewRoot().getLocale();
    var bundle = ResourceBundle.getBundle(baseName, locale);
    var messageTemplate = bundle.getString(key);
    var formatter = new MessageFormat(messageTemplate, locale);
    var messageText = formatter.format(arguments);

    var message = new FacesMessage(messageText);
    facesContext.addMessage(clientId, message);
  }
}
