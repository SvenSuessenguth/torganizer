package org.cc.torganizer.frontend.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.cc.torganizer.core.entities.Gender;

@FacesConverter(value = "genderJsfConverter")
public class GenderJsfConverter implements Converter<Gender> {

  @Override
  public Gender getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
    return Gender.byId(value);
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Gender gender) {
    return gender == null ? "" : gender.getId();
  }
}
