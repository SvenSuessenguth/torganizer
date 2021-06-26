package org.cc.torganizer.frontend.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import org.cc.torganizer.core.entities.System;

/**
 * Converting Systems from/to Strings.
 */
@FacesConverter(value = "systemConverter")
public class SystemConverter implements Converter<System> {

  @Override
  public System getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }

    try {
      return System.valueOf(value);
    } catch (IllegalArgumentException iaExc) {
      throw new ConverterException(iaExc);
    }
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, System system) {
    return system == null ? "" : system.name();
  }
}
