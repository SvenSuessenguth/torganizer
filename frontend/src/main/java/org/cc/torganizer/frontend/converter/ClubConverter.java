package org.cc.torganizer.frontend.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.persistence.ClubsRepository;

@FacesConverter(value = "clubConverter")
public class ClubConverter implements Converter<Club> {

  @Inject
  private ClubsRepository clubsRepository;

  @Override
  public Club getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
    if (value.isEmpty()) {
      return null;
    }
    Long id = Long.valueOf(value);
    return clubsRepository.read(id);
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Club club) {
    return club == null ? "" : "" + club.getId();
  }
}
