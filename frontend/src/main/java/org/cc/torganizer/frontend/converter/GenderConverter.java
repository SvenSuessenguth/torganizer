package org.cc.torganizer.frontend.converter;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.persistence.ClubsRepository;

@FacesConverter(value = "genderConverter")
public class GenderConverter implements Converter<Gender> {

  @Inject
  private FacesContext facesContext;

  @Override
  public Gender getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
    if (value.isEmpty()) {
      return null;
    }

    return Gender.valueOf(value);
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Gender gender) {
    // translate gender to language in UI
//    Locale locale = facesContext.getViewRoot().getLocale();
//    ResourceBundle genderI18N = ResourceBundle.getBundle("gender", locale);

    return gender == null ? "" : "" + gender.name();
  }
}
