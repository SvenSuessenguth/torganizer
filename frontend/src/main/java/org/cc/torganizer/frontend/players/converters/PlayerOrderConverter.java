package org.cc.torganizer.frontend.players.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.cc.torganizer.core.comparators.player.PlayerOrder;

@FacesConverter(value = "playerOrderConverter")
public class PlayerOrderConverter implements Converter<PlayerOrder> {

  @Override
  public PlayerOrder getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
    if (value.isEmpty()) {
      return null;
    }

    return PlayerOrder.valueOf(value);
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent,
                            PlayerOrder playerOrder) {
    return playerOrder == null ? "" : "" + playerOrder.name();
  }
}
