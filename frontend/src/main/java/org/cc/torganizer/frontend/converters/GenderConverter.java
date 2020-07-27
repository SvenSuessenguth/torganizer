package org.cc.torganizer.frontend.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.cc.torganizer.core.entities.Gender;

@FacesConverter(value = "genderConverter")
public class GenderConverter implements Converter<Gender> {

    @Override
    public Gender getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value.isEmpty()) {
            return null;
        }

        return Gender.valueOf(value);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Gender gender) {
        return gender == null ? "" : "" + gender.name();
    }
}
