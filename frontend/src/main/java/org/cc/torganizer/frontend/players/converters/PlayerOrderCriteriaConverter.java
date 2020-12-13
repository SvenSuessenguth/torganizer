package org.cc.torganizer.frontend.players.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.cc.torganizer.core.comparators.player.PlayerOrderCriteria;

@FacesConverter(value = "playerOrderCriteriaConverter")
public class PlayerOrderCriteriaConverter implements Converter<PlayerOrderCriteria> {

  @Override
  public PlayerOrderCriteria getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
    if (value.isEmpty()) {
      return null;
    }

    return PlayerOrderCriteria.valueOf(value);
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent,
                            PlayerOrderCriteria playerOrderCriteria) {
    return playerOrderCriteria == null ? "" : "" + playerOrderCriteria.name();
  }
}
