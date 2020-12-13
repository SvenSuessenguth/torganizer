package org.cc.torganizer.frontend;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.text.MessageFormat;
import java.util.Locale;
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
    Locale locale = facesContext.getViewRoot().getLocale();
    ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
    String messageTemplate = bundle.getString(key);
    MessageFormat formatter = new MessageFormat(messageTemplate, locale);
    String messageText = formatter.format(arguments);

    FacesMessage message = new FacesMessage(messageText);
    facesContext.addMessage(clientId, message);
  }
}
