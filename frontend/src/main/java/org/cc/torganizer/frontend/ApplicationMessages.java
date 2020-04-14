package org.cc.torganizer.frontend;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@RequestScoped
public class ApplicationMessages {

  @Inject
  private FacesContext facesContext;

  public void addMessage(String baseName, String key, Object[] arguments) {
    Locale locale = facesContext.getViewRoot().getLocale();
    ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
    String messageTemplate = bundle.getString(key);
    MessageFormat formatter = new MessageFormat(messageTemplate, locale);
    String messageText = formatter.format(arguments);

    FacesMessage message = new FacesMessage(messageText);
    facesContext.addMessage(null, message);
  }
}
