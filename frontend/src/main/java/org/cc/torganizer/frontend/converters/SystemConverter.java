package org.cc.torganizer.frontend.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.cc.torganizer.core.entities.System;

@FacesConverter(value = "systemConverter")
public class SystemConverter implements Converter<System> {

  @Override
  public System getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
    if (value.isEmpty()) {
      return null;
    }

    return System.valueOf(value);
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, System system) {
    return system == null ? "" : "" + system.name();
  }
}
