package org.cc.torganizer.frontend.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.cc.torganizer.core.entities.OpponentType;

@FacesConverter(value = "opponentTypeConverter")
public class OpponentTypeConverter implements Converter<OpponentType> {

  @Override
  public OpponentType getAsObject(FacesContext facesContext, UIComponent uiComponent,
                                  String value) {
    if (value.isEmpty()) {
      return null;
    }

    return OpponentType.valueOf(value);
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent,
                            OpponentType opponentType) {
    return opponentType == null ? "" : "" + opponentType.name();
  }
}
