package org.cc.torganizer.frontend.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import org.cc.torganizer.core.entities.OpponentType;

@FacesConverter(value = "opponentTypeConverter")
public class OpponentTypeConverter implements Converter<OpponentType> {

  @Override
  public OpponentType getAsObject(FacesContext facesContext, UIComponent uiComponent,
                                  String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }

    try {
      return OpponentType.valueOf(value.trim());
    } catch (IllegalArgumentException iaExc) {
      throw new ConverterException(iaExc);
    }
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent,
                            OpponentType opponentType) {
    return opponentType == null ? "" : opponentType.name();
  }
}
